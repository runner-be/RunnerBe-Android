package com.applemango.runnerbe.screen.fragment.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.applemango.runnerbe.screen.fragment.mypage.joinpost.JoinPostFragment
import com.applemango.runnerbe.screen.fragment.mypage.mypost.MyPostFragment

/**
 * author: 두루
 */
class MyPageAdapter (fragment: MyPageFragment): FragmentStateAdapter(fragment) {

    var fragmentList = mutableListOf<Fragment>(MyPostFragment(), JoinPostFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemId(position: Int): Long {
        // generate new id
        return fragmentList[position].hashCode().toLong()
    }

    fun setFragment(position: Int, refresh: Fragment){
        fragmentList[position] = refresh
    }

    override fun containsItem(itemId: Long): Boolean {
        // false if item is changed
        return fragmentList.find { it.hashCode().toLong() == itemId } != null
    }
}