package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.model.UserModel

interface AttendanceAccessionClickListener {
    fun onAcceptClick(user: UserModel)
    fun onRefuseClick(user: UserModel)
}