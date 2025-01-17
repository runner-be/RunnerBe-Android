package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import com.applemango.runnerbe.presentation.model.UserModel

interface WaitingRunnerClickListener {
    fun onProfileClicked(userInfo: UserModel)
    fun onRefuseClicked(userInfo: UserModel)
    fun onAcceptClicked(userInfo: UserModel)
}