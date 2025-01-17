package com.applemango.runnerbe.presentation.model

data class JoinedRunnerModel(
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String?,
    val logId: String?,
    val isOpened: Int?,
    val stampCode: String?,
    val isCaptain: Int
)