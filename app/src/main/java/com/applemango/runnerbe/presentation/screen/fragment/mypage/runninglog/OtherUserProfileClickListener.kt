package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem

interface OtherUserProfileClickListener {
    fun onProfileImageClicked(userId: Int)
    fun onProfileClicked(position: Int, targetUserId: Int, stamp: StampItem?)
}