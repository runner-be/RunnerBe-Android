package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceSelectItem

interface PaceSelectListener {
    fun itemClick(paceSelectItem: PaceSelectItem)
}