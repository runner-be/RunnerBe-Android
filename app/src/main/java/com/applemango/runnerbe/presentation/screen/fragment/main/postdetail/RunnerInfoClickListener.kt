package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import com.applemango.runnerbe.presentation.model.UserModel

fun interface RunnerInfoClickListener {
    fun onRunnerInfoClicked(user: UserModel)
}