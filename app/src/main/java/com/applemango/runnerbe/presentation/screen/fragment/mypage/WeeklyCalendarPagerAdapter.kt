package com.applemango.runnerbe.presentation.screen.fragment.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.time.LocalDate

class WeeklyCalendarPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var startDate: LocalDate = LocalDate.now()

    override fun getItemCount(): Int {
        return SIZE_PAGE
    }

    override fun createFragment(position: Int): Fragment {
        val thisWeekStartDate = startDate.minusWeeks(POSITION_PAGE_MAX - position.toLong())
        return WeeklyCalendarFragment.newInstance(thisWeekStartDate, position)
    }

    companion object {
        private const val SIZE_PAGE = 3
        private const val POSITION_PAGE_MAX = 2
    }
}