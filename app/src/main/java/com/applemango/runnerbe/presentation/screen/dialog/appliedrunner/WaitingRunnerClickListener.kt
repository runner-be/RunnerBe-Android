package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import com.applemango.runnerbe.data.dto.UserInfo

interface WaitingRunnerClickListener {
    fun onProfileClicked(userInfo: UserInfo)
    fun onRefuseClicked(userInfo: UserInfo)
    fun onAcceptClicked(userInfo: UserInfo)
}