package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.groupprofile

import com.applemango.presentation.ui.screen.dialog.stamp.StampItem

data class ProfileItem(
    val image: Int?,
    val name: String,
    val isLeader: Boolean,
    var stamp: StampItem?
)