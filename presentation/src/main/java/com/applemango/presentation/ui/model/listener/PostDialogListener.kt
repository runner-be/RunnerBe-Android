package com.applemango.presentation.ui.model.listener

interface PostDialogListener {
    fun moveToMessage(roomId: Int, repUserName : String?)
    fun dismiss()
}