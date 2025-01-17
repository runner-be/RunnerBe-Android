package com.applemango.runnerbe.presentation.model

data class MemberStampModel(
    val userId: Int,
    val logId: Int?,
    val nickname: String,
    val profileImageUrl: String?,
    val stampCode: String,
)
