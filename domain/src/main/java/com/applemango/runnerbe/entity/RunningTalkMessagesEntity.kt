package com.applemango.runnerbe.entity

data class RunningTalkMessagesEntity(
    val roomInfo : List<RoomInfo>,
    val messages : List<RunningTalkMessageEntity>
)

data class RoomInfo(
    val talkTitle : String,
    val pace: String
)