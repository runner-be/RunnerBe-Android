package com.applemango.runnerbe.presentation.screen.fragment.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.time.LocalDate

class WeeklyCalendarPagerAdapter(
    fragment: Fragment,
    private val startDate: LocalDate
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    // TODO()
    override fun createFragment(position: Int): Fragment {
        val dateForPage = startDate.minusWeeks(1 - position.toLong())
        return WeeklyCalendarFragment.newInstance(dateForPage, emptyList())
    }
}