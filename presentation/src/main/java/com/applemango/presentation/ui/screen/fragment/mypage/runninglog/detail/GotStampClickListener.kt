package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.detail

import com.applemango.presentation.ui.model.MemberStampModel

fun interface GotStampClickListener {
    fun onGotStampClicked(item: MemberStampModel)
}