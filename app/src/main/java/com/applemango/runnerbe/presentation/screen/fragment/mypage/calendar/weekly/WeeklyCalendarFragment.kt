package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.weekly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.response.GatheringData
import com.applemango.runnerbe.data.network.response.RunningLog
import com.applemango.runnerbe.data.network.response.TotalCount
import com.applemango.runnerbe.databinding.FragmentWeeklyCalendarBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.mypage.MyPageViewModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.initWeekDays
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileFragmentDirections
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeeklyCalendarAdapter()
        setupWeeklyRunningLogs()
        viewModel.fetchUserWeeklyRunningLog(LocalDate.now(), targetUserId)
    }

    private fun setupWeeklyRunningLogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPageInfo.collectLatest { result ->
                    if (result == null) return@collectLatest

                    val thisWeekMonday =
                        LocalDate.now().minusWeeks(POSITION_DEFAULT - position.toLong())
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    val parsedRunningLogs = combineGatheringDataToRunningLogs(result.gatheringDays, result.runningLog)
                    val thisWeekLogs = parseRunningLogs(thisWeekMonday, parsedRunningLogs)
                    val (groupCount, personalCount) = result.totalCount ?: TotalCount(0, 0)

                    val openedLogs = if (isOtherUserProfile) thisWeekLogs.filter {
                        it.isOpened == 1
                    } else thisWeekLogs

                    binding.tvStampWeekly.text = if (personalCount == 0 && groupCount == 0) {
                        getString(R.string.lets_add_stamp)
                    } else {
                        getString(
                            R.string.calendar_monthly_statistic,
                            groupCount, personalCount
                        )
                    }
                    weeklyCalendarAdapter.submitList(initWeekDays(thisWeekMonday, openedLogs))
                }
            }
        }
    }

    private fun combineGatheringDataToRunningLogs(
        gatheringDataList: List<GatheringData>,
        runningLogList: List<RunningLog>
    ): List<RunningLog> {
        val groupLogData = gatheringDataList.associateBy { it.date }
        val runningLogs = runningLogList.toMutableList()
        runningLogs.forEach { runningLog ->
            groupLogData[runningLog.runnedDate]?.let { gatheringData ->
                runningLog.gatheringId = gatheringData.gatheringId
            }
        }
        return runningLogs
    }

    private fun parseRunningLogs(
        startDate: LocalDate,
        runningLogs: List<RunningLog>
    ): List<RunningLog> {
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
                        || itemStampCode == StampItem.availableStampItem.code) {
                        return@setOnDateClickListener
                    } else {
                        navigate(
                            OtherUserProfileFragmentDirections.actionUserProfileFragmentToRunningLogDetailFragment(
                                targetUserId,
                                item.runningLog.logId
                            )
                        )
                        return@setOnDateClickListener
                    }
                }

                viewModel.updateWeeklyViewPagerPosition(position)
                val runningLog = item.runningLog

                if (runningLog != null) {
                    val userId = RunnerBeApplication.mTokenPreference.getUserId()
                    navigate(
                        MainFragmentDirections.actionMainFragmentToRunningLogDetailFragment(
                            userId,
                            runningLog.logId
                        )
                    )
                } else {
                    item.date?.let { itemDate ->
                        navigate(
                            MainFragmentDirections.actionMainFragmentToRunningLogFragment(
                                itemDate.toString(),
                                null,
                                null
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

        fun newInstance(date: LocalDate, position: Int, isOtherUser: Boolean, userId: Int): WeeklyCalendarFragment {
            val fragment = WeeklyCalendarFragment()
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            args.putInt(ARG_POSITION, position)
            args.putBoolean(ARG_IS_OTHER_USER, isOtherUser)
            args.putInt(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }
}