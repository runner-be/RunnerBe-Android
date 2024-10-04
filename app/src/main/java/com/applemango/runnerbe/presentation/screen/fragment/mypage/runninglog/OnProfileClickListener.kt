package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem

fun interface OnProfileClickListener {
    fun onProfileClicked(position: Int, stamp: StampItem?)
}