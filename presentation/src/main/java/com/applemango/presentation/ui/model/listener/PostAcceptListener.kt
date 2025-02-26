package com.applemango.presentation.ui.model.listener

import com.applemango.presentation.ui.model.UserModel

interface PostAcceptListener {
    fun onAcceptClick(userInfo: UserModel)
    fun onRefuseClick(userInfo: UserModel)
}