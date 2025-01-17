package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import com.applemango.runnerbe.presentation.model.MemberStampModel

fun interface GotStampClickListener {
    fun onGotStampClicked(item: MemberStampModel)
}