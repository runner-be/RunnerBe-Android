package com.applemango.runnerbe.presentation.model

data class RunningTalkRoomModel(
    val roomId: Int,
    val title: String,
    val repUserName: String,
    val profileImageUrl: String,
    val recentMessage: String
)