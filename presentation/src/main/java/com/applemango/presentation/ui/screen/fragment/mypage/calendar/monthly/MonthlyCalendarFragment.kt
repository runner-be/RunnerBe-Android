package com.applemango.presentation.ui.screen.fragment.mypage.calendar.monthly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.applemango.presentation.ui.model.GatheringData
import com.applemango.presentation.ui.model.RunningLogModel
import com.applemango.presentation.ui.screen.dialog.yearmonthselect.YearMonthSelectData
import com.applemango.presentation.ui.screen.dialog.yearmonthselect.YearMonthSelectDialog
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.initYearMonthDays
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.weekly.DayOfWeekAdapter
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentMonthlyCalendarBinding
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MonthlyCalendarFragment :
    BaseFragment<FragmentMonthlyCalendarBinding>(R.layout.fragment_monthly_calendar) {

    private val viewModel: MonthlyCalendarViewModel by viewModels()

    private val navArgs: MonthlyCalendarFragmentArgs by navArgs()

    private var _monthlyCalendarAdapter: MonthlyCalendarAdapter? = null
    private val monthlyCalendarAdapter get() = _monthlyCalendarAdapter!!

    private var _dayOfWeekAdapter: DayOfWeekAdapter? = null
    private val dayOfWeekAdapter get() = _dayOfWeekAdapter!!

    private var isOtherUserProfile: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseNavArgs(navArgs)
        initYearMonthSpinner()
        initDayOfWeekAdapter()
        initCalendarAdapter()
        setListeners()
        setupStampStatistic()
        setupCalendarFlow()
    }

    private fun parseNavArgs(navArgs: MonthlyCalendarFragmentArgs) {
        val targetUserId = navArgs.userId
        isOtherUserProfile = (navArgs.isOtherUser == 1)
        viewModel.updateTargetUserId(targetUserId)
    }

    private fun setListeners() {
        with(binding) {
            compositeDisposable.addAll(
                llMonth.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val (selectedYear, selectedMonth) = viewModel.selectedYearMonth.value
                        YearMonthSelectDialog.createAndShow(
                            childFragmentManager,
                            YearMonthSelectData(selectedYear.toString(), selectedMonth.toString())
                        ) { year, month ->
                            binding.tvMonth.text = "${year}년 ${month}월"
                            viewModel.updateSelectedYearMonth(year, month)
                        }
                    },
                btnClose.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        goBack()
                    }
            )
        }
    }

    private fun combineGatheringDataToRunningLogs(
        gatheringDataList: List<GatheringData>,
        runningLogList: List<RunningLogModel>
    ): List<RunningLogModel> {
        val runningLogsMap = runningLogList.associateBy { it.runnedDate.toLocalDate() }
        val logsDateSet = runningLogsMap.keys
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupCalendarFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedYearMonthFlow.collectLatest { response ->
                    val monthlyStatistic = response.totalCount
                    val monthlyGatheringData = response.gatheringDays
                    val monthlyLogList = response.runningLog

                    val parsedRunningLogs = combineGatheringDataToRunningLogs(
                        monthlyGatheringData,
                        monthlyLogList
                    )

                    binding.tvStampMonthly.text = if (monthlyStatistic != null) {
                        getString(
                            R.string.calendar_monthly_statistic,
                            monthlyStatistic.groupRunningCount,
                            monthlyStatistic.personalRunningCount
                        )
                    } else {
                        getString(R.string.calendar_monthly_statistic_empty)
                    }

                    val (selectedYear, selectedMonth) = viewModel.selectedYearMonth.value
                    val newCalendarData = initYearMonthDays(selectedYear, selectedMonth, parsedRunningLogs)
                    monthlyCalendarAdapter.submitList(newCalendarData)
                }
            }
        }
    }

    private fun setupStampStatistic() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stampStatistic.collectLatest {
                    val crewCount = it["크루"]
                    val personalCount = it["개인"]
                    binding.tvStampMonthly.text =
                        getString(R.string.calendar_monthly_statistic, crewCount, personalCount)
                }
            }
        }
    }

    private fun initYearMonthSpinner() {
        with(binding.tvMonth) {
            val today = LocalDate.now()
            text = getString(R.string.calendar_selected_year_month, today.year, today.monthValue)
        }
    }

    private fun initDayOfWeekAdapter() {
        with(binding.rcvCalendarDayOfWeek) {
            _dayOfWeekAdapter = DayOfWeekAdapter()
            adapter = dayOfWeekAdapter.apply {
                submitList(getDayOfWeekList())
            }
            layoutManager = GridLayoutManager(context, 7)
        }
    }

    private fun initCalendarAdapter() {
        with(binding.rcvCalendarDate) {
            _monthlyCalendarAdapter = MonthlyCalendarAdapter()
            adapter = monthlyCalendarAdapter.apply {
                setIsOtherUser(isOtherUserProfile)
                setOnDateClickListener { item ->
                    val runningLog = item.runningLog

                    if (runningLog?.logId != null) {
                        val targetUserId = viewModel.targetUserId.value

                        if (targetUserId != null) {
                            navigate(
                                MonthlyCalendarFragmentDirections.actionMonthlyCalendarFragmentToRunningLogDetailFragment(
                                    targetUserId,
                                    runningLog.logId,
                                    navArgs.isOtherUser
                                )
                            )
                        }
                    } else {
                        if (isOtherUserProfile) return@setOnDateClickListener

                        val date = item.date!!
                        navigate(
                            MonthlyCalendarFragmentDirections.actionMonthlyCalendarFragmentToRunningLogFragment(
                                date.toString(),
                                null,
                                item.runningLog?.gatheringId.toString()
                            )
                        )
                    }
                }
            }
            layoutManager = GridLayoutManager(context, 7)
        }
    }

    private fun getDayOfWeekList(): List<String> {
        return listOf(
            getString(R.string.monday),
            getString(R.string.tuesday),
            getString(R.string.wednesday),
            getString(R.string.thursday),
            getString(R.string.friday),
            getString(R.string.saturday),
            getString(R.string.sunday),
        )
    }
}