package com.applemango.runnerbe.data.dto

data class RunningTalkRoomDto(
    val roomId: Int,
    val title: String,
    val repUserName: String,
    val profileImageUrl: String,
    val recentMessage: String
)