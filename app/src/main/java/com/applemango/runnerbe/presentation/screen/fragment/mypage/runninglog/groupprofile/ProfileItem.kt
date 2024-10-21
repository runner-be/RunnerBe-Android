package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem

data class ProfileItem(
    val image: Int?,
    val name: String,
    val isLeader: Boolean,
    var stamp: StampItem?
)