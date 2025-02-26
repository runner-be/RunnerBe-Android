package com.applemango.domain.entity

import java.time.ZonedDateTime

data class RunningLogDetailEntity(
    val runningLog: RunningLogDetail,
    val gatheringCount: Int,
    val gotStamp: List<MemberStamp>
)

data class RunningLogDetail (
    val status: String,
    val runnedDate: ZonedDateTime,
    val userId: Int,
    val nickname: String,
    val gatheringId: Int?,
    val stampCode: String,
    val contents: String,
    val imageUrl: String?,
    val weatherDegree: Int?,
    val weatherIcon: String?,
    val isOpened: Int,
)

data class MemberStamp (
    val userId: Int,
    val logId: Int?,
    val nickname: String,
    val profileImageUrl: String?,
    val stampCode: String,
)