package com.applemango.presentation.ui.model

import java.time.ZonedDateTime

data class RunningLogDetailModel(
    val runningLog: RunningLogDetail,
    val gatheringCount: Int,
    val gotStamp: List<MemberStampModel>
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
    val weatherCode: String?,
    val isOpened: Int,
)