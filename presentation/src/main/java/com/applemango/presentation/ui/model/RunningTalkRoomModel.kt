package com.applemango.presentation.ui.model

data class RunningTalkRoomModel(
    val roomId: Int,
    val title: String,
    val repUserName: String,
    val profileImageUrl: String,
    val recentMessage: String
)