package com.applemango.presentation.ui.screen.fragment.chat.detail.uistate

sealed class RunningTalkItem(val messageId: Int) {
    data class MessageTalkItem(val id: Int, val message: String) : RunningTalkItem(id)
    data class ImageTalkItem(val id: Int, val imgUrl: String): RunningTalkItem(id)
}