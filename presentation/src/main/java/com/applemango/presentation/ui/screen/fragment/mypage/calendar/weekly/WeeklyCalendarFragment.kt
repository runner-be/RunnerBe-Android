package com.applemango.presentation.ui.screen.fragment.mypage.calendar.weekly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.applemango.presentation.ui.model.GatheringData
import com.applemango.presentation.ui.model.RunningLogModel
import com.applemango.presentation.ui.model.TotalCount
import com.applemango.presentation.ui.screen.dialog.stamp.StampItem
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.screen.fragment.mypage.MyPageViewModel
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.initWeekDays
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentWeeklyCalendarBinding
import com.applemango.presentation.ui.screen.fragment.main.MainFragmentDirections
import com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@AndroidEntryPoint
class WeeklyCalendarFragment() :
    BaseFragment<FragmentWeeklyCalendarBinding>(R.layout.fragment_weekly_calendar) {

    @Inject
    lateinit var weeklyCalendarAdapter: WeeklyCalendarAdapter

    private var positionChangeListener: VpPositionChangeListener? = null

    private val viewModel: MyPageViewModel by activityViewModels()
    private val position: Int by lazy {
        arguments?.getInt(ARG_POSITION) ?: POSITION_DEFAULT
    }
    private val isOtherUserProfile: Boolean by lazy {
        arguments?.getBoolean(ARG_IS_OTHER_USER) ?: false
    }
    private val targetUserId: Int by lazy {
        arguments?.getInt(ARG_USER_ID) ?: -1
    }
    private var counts: Pair<Int, Int> = Pair(0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeeklyCalendarAdapter()
        setupWeeklyRunningLogs()
        viewModel.fetchUserRunningLog(LocalDate.now(), targetUserId)
    }

    override fun onResume() {
        super.onResume()
        positionChangeListener?.onPositionChanged(counts.first, counts.second)
        updateCurrentMondayMonth()
    }

    /**
     * 1) 이번주 월요일이 11월 30일이라면
     * 2024년 11월이라고 표기되어야 함
     * 2) 이번주 월요일이 12월 2일이라면
     * 2024년 12월이라고 표기되어야 함
     */
    private fun updateCurrentMondayMonth() {
        val thisWeekMonday: LocalDate =
            LocalDate.now().minusWeeks(POSITION_DEFAULT - position.toLong())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val thisWeekMondayYear = thisWeekMonday.year
        val thisWeekMondayMonth = thisWeekMonday.monthValue
        viewModel.updateCurrentMondayMonth(thisWeekMondayYear, thisWeekMondayMonth)
    }

    /**
     * 서버 API 변경 예정
     * 현재는 이전달 + 이번달 데이터 조회 -> 변환 -> 병합 과정을 거쳐야 해서 로직이 복잡함
     */
    private fun setupWeeklyRunningLogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.runningLogResult.collectLatest { runningResultMap ->
                    val thisWeekMonday: LocalDate =
                        LocalDate.now().minusWeeks(POSITION_DEFAULT - position.toLong())
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

                    val thisWeekMondayMonth = thisWeekMonday.monthValue
                    val thisMonth = LocalDate.now().monthValue
                    when (thisWeekMondayMonth != thisMonth) {
                        true -> {
                            /**
                             * 이번주 월요일이 이전 달인 경우
                             * ex) 오늘은 2024년 11월 1일 (금요일)이지만,
                             *     이번주 월요일은 2024년 11월 28일(월요일)인 경우
                             */
                            val prevMonthGatheringDataList =
                                runningResultMap[thisWeekMondayMonth]?.gatheringDays ?: emptyList()
                            val thisMonthGatheringDataList =
                                runningResultMap[thisMonth]?.gatheringDays ?: emptyList()

                            val prevMonthRunningLogList =
                                runningResultMap[thisWeekMondayMonth]?.runningLog ?: emptyList()
                            val thisMonthRunningLogList =
                                runningResultMap[thisMonth]?.runningLog ?: emptyList()

                            val parsedRunningLogs = combineGatheringDataToRunningLogs(
                                prevMonthGatheringDataList + thisMonthGatheringDataList,
                                prevMonthRunningLogList + thisMonthRunningLogList
                            )
                            val thisWeekLogs = parseRunningLogs(thisWeekMonday, parsedRunningLogs)
                            val (groupCount, personalCount) = runningResultMap[thisWeekMondayMonth]?.totalCount
                                ?: TotalCount(0, 0)

                            counts = Pair(groupCount, personalCount)
                            positionChangeListener?.onPositionChanged(counts.first, counts.second)
                            weeklyCalendarAdapter.submitList(
                                initWeekDays(
                                    thisWeekMonday,
                                    thisWeekLogs
                                )
                            )
                        }

                        false -> {
                            val thisMonthGatheringDataList =
                                runningResultMap[thisMonth]?.gatheringDays ?: emptyList()
                            val thisMonthRunningLogList =
                                runningResultMap[thisMonth]?.runningLog ?: emptyList()

                            val parsedRunningLogs =
                                combineGatheringDataToRunningLogs(
                                    thisMonthGatheringDataList,
                                    thisMonthRunningLogList
                                )
                            val thisWeekLogs = parseRunningLogs(thisWeekMonday, parsedRunningLogs)
                            val (groupCount, personalCount) = runningResultMap[thisMonth]?.totalCount
                                ?: TotalCount(0, 0)

                            counts = Pair(groupCount, personalCount)
                            positionChangeListener?.onPositionChanged(counts.first, counts.second)
                            weeklyCalendarAdapter.submitList(
                                initWeekDays(
                                    thisWeekMonday,
                                    thisWeekLogs
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun combineGatheringDataToRunningLogs(
        gatheringDataList: List<GatheringData>,
        runningLogList: List<RunningLogModel>
    ): List<RunningLogModel> {
        val runningLogsMap = runningLogList.associateBy { it.runnedDate.toLocalDate() }
        val logsDateSet = runningLogsMap.keys // 이미 러닝로그가 작성된 날짜들
        val gatheredMap = gatheringDataList
            .filter { it.date.toLocalDate() !in logsDateSet }
            .associateBy({ it.date },
                { gatheringData ->
                    RunningLogModel(
                        null,
                        gatheringId = gatheringData.gatheringId,
                        runnedDate = gatheringData.date,
                        null,
                        1
                    )
                }
            )
        val combinedMap = gatheredMap + runningLogsMap
        return combinedMap.values.toList()
    }

    private fun parseRunningLogs(
        startDate: LocalDate,
        runningLogs: List<RunningLogModel>
    ): List<RunningLogModel> {
        val thisWeekEndDate = startDate.plusDays(6)
        return runningLogs
            .filter {
                it.runnedDate.toLocalDate() in startDate..thisWeekEndDate
            }
    }

    private fun initWeeklyCalendarAdapter() {
        binding.rcvCalendarWeekly.apply {
            adapter = weeklyCalendarAdapter
            weeklyCalendarAdapter.setIsOtherUser(isOtherUserProfile)
            weeklyCalendarAdapter.setOnDateClickListener { item ->
                val itemStampCode = item.runningLog?.stampCode
                if (isOtherUserProfile) {
                    if (itemStampCode == null
                        || itemStampCode == StampItem.unavailableStampItem.code
                        || itemStampCode == StampItem.availableStampItem.code
                    ) {
                        return@setOnDateClickListener
                    } else {
                        navigate(
                            OtherUserProfileFragmentDirections.actionUserProfileFragmentToRunningLogDetailFragment(
                                targetUserId,
                                item.runningLog.logId
                                    ?: throw IllegalArgumentException("RunningLogId is NULL"),
                                if (isOtherUserProfile) 1 else 0
                            )
                        )
                        return@setOnDateClickListener
                    }
                }

//                viewModel.updateWeeklyViewPagerPosition(position)
                val runningLog = item.runningLog

                if (runningLog?.logId != null) {
                    navigate(
                        MainFragmentDirections.actionMainFragmentToRunningLogDetailFragment(
                            viewModel.userId.value,
                            runningLog.logId,
                            0
                        )
                    )
                } else {
                    item.date?.let { itemDate ->
                        navigate(
                            MainFragmentDirections.actionMainFragmentToRunningLogFragment(
                                itemDate.toString(),
                                null,
                                item.runningLog?.gatheringId.toString()
                            )
                        )
                    }
                }
            }
            itemAnimator = null
            layoutManager = GridLayoutManager(context, 7)
        }
    }

    companion object {
        private const val ARG_DATE = "arg_date"
        private const val ARG_POSITION = "arg_position"
        private const val ARG_IS_OTHER_USER = "arg_is_other_user"
        private const val ARG_USER_ID = "arg_user_id"
        private const val POSITION_DEFAULT = 2

        fun newInstance(
            date: LocalDate,
            position: Int,
            isOtherUser: Boolean,
            userId: Int,
            positionChangeListener: VpPositionChangeListener
        ): WeeklyCalendarFragment {
            val fragment = WeeklyCalendarFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(ARG_DATE, date)
                putInt(ARG_POSITION, position)
                putBoolean(ARG_IS_OTHER_USER, isOtherUser)
                putInt(ARG_USER_ID, userId)
            }
            fragment.positionChangeListener = positionChangeListener
            return fragment
        }
    }
}