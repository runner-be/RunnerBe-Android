package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.model.UserModel


interface PostAcceptListener {
    fun onAcceptClick(userInfo: UserModel)
    fun onRefuseClick(userInfo: UserModel)
}