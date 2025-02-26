package com.applemango.presentation.ui.screen.dialog.appliedrunner

import com.applemango.presentation.ui.model.UserModel

interface WaitingRunnerClickListener {
    fun onProfileClicked(userInfo: UserModel)
    fun onRefuseClicked(userInfo: UserModel)
    fun onAcceptClicked(userInfo: UserModel)
}