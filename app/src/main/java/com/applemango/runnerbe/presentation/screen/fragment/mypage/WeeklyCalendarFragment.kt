package com.applemango.runnerbe.presentation.screen.fragment.mypage

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentWeeklyCalendarBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.WeeklyCalendarAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.initWeekDays
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class WeeklyCalendarFragment : BaseFragment<FragmentWeeklyCalendarBinding>(R.layout.fragment_weekly_calendar) {

    @Inject
    lateinit var weeklyCalendarAdapter: WeeklyCalendarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeeklyCalendarAdapter()
    }

    private fun initWeeklyCalendarAdapter() {
        binding.rcvCalendarWeekly.apply {
            adapter = weeklyCalendarAdapter
            val (startDate, weeklyStampList) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Pair(
                    arguments?.getSerializable(ARG_DATE, LocalDate::class.java),
                    arguments?.getParcelableArrayList(ARG_STAMP_LIST, StampItem::class.java)
                )
            } else {
                Pair(
                    arguments?.getSerializable(ARG_DATE) as LocalDate,
                    arguments?.getParcelableArrayList(ARG_STAMP_LIST)
                )
            }
//            weeklyCalendarAdapter.submitList(initWeekDays(startDate, weeklyStampList))
        }
    }

    companion object {
        private const val ARG_DATE = "arg_date"
        private const val ARG_STAMP_LIST = "arg_stamp_list"

        fun newInstance(date: LocalDate, stampList: List<StampItem>): WeeklyCalendarFragment {
            val fragment = WeeklyCalendarFragment()
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            args.putParcelableArrayList(ARG_STAMP_LIST, ArrayList(stampList))
            fragment.arguments = args
            return fragment
        }
    }
}