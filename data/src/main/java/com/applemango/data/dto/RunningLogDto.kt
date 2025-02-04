package com.applemango.data.dto

import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class RunningLogDto(
    @Json(name = "logId") val logId: Int?,
    @Json(name = "gatheringId") var gatheringId: Int?,
    @Json(name = "runnedDate") val runnedDate: ZonedDateTime,
    @Json(name = "stampCode") val stampCode: String?,
    @Json(name = "isOpened") val isOpened: Int,
)
