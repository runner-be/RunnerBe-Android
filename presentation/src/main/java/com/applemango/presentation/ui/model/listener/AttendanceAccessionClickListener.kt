package com.applemango.presentation.ui.model.listener

import com.applemango.presentation.ui.model.UserModel

interface AttendanceAccessionClickListener {
    fun onAcceptClick(user: UserModel)
    fun onRefuseClick(user: UserModel)
}