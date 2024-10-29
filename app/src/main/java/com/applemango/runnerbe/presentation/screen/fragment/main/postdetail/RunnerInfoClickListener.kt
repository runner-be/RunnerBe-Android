package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import com.applemango.runnerbe.data.dto.UserInfo

fun interface RunnerInfoClickListener {
    fun onRunnerInfoClicked(userInfo: UserInfo)
}