package com.applemango.domain.entity

data class RunningTalkRoomEntity(
    val roomId: Int,
    val title: String,
    val repUserName: String,
    val profileImageUrl: String,
    val recentMessage: String
)