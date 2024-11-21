package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.weekly

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.time.LocalDate

class WeeklyCalendarPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val isOtherUser: Boolean
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var startDate: LocalDate = LocalDate.now()

    override fun getItemCount(): Int {
        return SIZE_PAGE
    }

    override fun createFragment(position: Int): Fragment {
        val thisWeekStartDate = startDate.minusWeeks(POSITION_PAGE_MAX - position.toLong())
        return WeeklyCalendarFragment.newInstance(thisWeekStartDate, position, isOtherUser)
    }

    companion object {
        private const val SIZE_PAGE = 3
        private const val POSITION_PAGE_MAX = 2
    }
}