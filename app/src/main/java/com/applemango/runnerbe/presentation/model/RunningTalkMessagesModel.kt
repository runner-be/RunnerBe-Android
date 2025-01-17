package com.applemango.runnerbe.presentation.model

data class RunningTalkMessagesModel(
    val roomInfo : List<RoomInfoModel>,
    val messages : List<RunningTalkMessageModel>
)

data class RoomInfoModel(
    val talkTitle : String,
    val pace: String
)