package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import com.applemango.presentation.ui.screen.dialog.stamp.StampItem

interface OtherUserProfileClickListener {
    fun onProfileImageClicked(userId: Int)
    fun onProfileClicked(position: Int, targetUserId: Int, stamp: StampItem?)
    fun onProfileLogClicked(userId: Int, logId: String?)
}