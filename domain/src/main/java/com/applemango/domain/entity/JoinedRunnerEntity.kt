package com.applemango.domain.entity

data class JoinedRunnerEntity(
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String?,
    val logId: String?,
    val isOpened: Int?,
    var stampCode: String?,
    val isCaptain: Int,
)
