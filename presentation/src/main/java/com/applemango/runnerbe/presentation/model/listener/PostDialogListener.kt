package com.applemango.runnerbe.presentation.model.listener

interface PostDialogListener {
    fun moveToMessage(roomId: Int, repUserName : String?)
    fun dismiss()
}