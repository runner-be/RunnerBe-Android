package com.applemango.presentation.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.applemango.presentation.ui.model.type.MainBottomTab
import com.applemango.presentation.ui.screen.fragment.bookmark.BookMarkFragment
import com.applemango.presentation.ui.screen.fragment.chat.RunningTalkFragment
import com.applemango.presentation.ui.screen.fragment.map.RunnerMapFragment
import com.applemango.presentation.ui.screen.fragment.mypage.MyPageFragment

/**
 * 메인 페이지의 탭 화면의 fragment 정의 어댑터입니다.
 * 탭 페이지는 MainBottomTab으로 관리합니다.
 * author: niaka
 */
class MainFragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val tabIconIdList: List<Int>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = tabIconIdList.size

    override fun createFragment(position: Int): Fragment {
        return when (tabIconIdList[position]) {
            MainBottomTab.MAP.iconResourceId -> RunnerMapFragment()
            MainBottomTab.BOOK_MARK.iconResourceId -> BookMarkFragment()
            MainBottomTab.MESSAGE.iconResourceId -> RunningTalkFragment()
            MainBottomTab.MY.iconResourceId -> MyPageFragment()
            else -> RunnerMapFragment()
        }
    }
}