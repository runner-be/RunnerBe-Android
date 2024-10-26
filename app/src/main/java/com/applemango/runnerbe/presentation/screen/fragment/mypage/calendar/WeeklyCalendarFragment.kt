package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.response.RunningLog
import com.applemango.runnerbe.databinding.FragmentWeeklyCalendarBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.mypage.MyPageViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeeklyCalendarAdapter()
        setupWeeklyRunningLogs()
    }

    private fun setupWeeklyRunningLogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.thisWeekRunningLogFlow.collectLatest {
                    val thisWeekMonday =
                        LocalDate.now().minusWeeks(POSITION_DEFAULT - position.toLong())
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    val thisWeekLogs = parseRunningLogs(thisWeekMonday, it.runningLog)
                    weeklyCalendarAdapter.submitList(initWeekDays(thisWeekMonday, thisWeekLogs))
                }
            }
        }
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
            weeklyCalendarAdapter.setOnDateClickListener { item ->
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
        private const val POSITION_DEFAULT = 2

        fun newInstance(date: LocalDate, position: Int): WeeklyCalendarFragment {
            val fragment = WeeklyCalendarFragment()
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}