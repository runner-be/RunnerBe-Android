package com.applemango.presentation.ui.model

data class MemberStampModel(
    val userId: Int,
    val logId: Int?,
    val nickname: String,
    val profileImageUrl: String?,
    val stampCode: String,
)
