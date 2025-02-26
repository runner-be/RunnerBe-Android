package com.applemango.presentation.ui.model

data class RunningTalkMessagesModel(
    val roomInfo : List<RoomInfoModel>,
    val messages : List<RunningTalkMessageModel>
)

data class RoomInfoModel(
    val talkTitle : String,
    val pace: String
)