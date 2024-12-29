package com.applemango.runnerbe.data.network.response

import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class MonthlyStampResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: RunningLogResult
)

data class RunningLogResult (
    @Json(name = "totalCount") val totalCount: TotalCount?,
    @Json(name = "myRunningLog") val runningLog: List<RunningLog>,
    @Json(name = "isExistGathering") val gatheringDays: List<GatheringData>,
)

data class GatheringData (
    @Json(name = "gatheringId") val gatheringId: Int,
    @Json(name = "gatheringTime") val date: ZonedDateTime
)

data class TotalCount(
    @Json(name = "groupRunningCount") val groupRunningCount: Int,
    @Json(name = "personalRunningCount") val personalRunningCount: Int
)

data class RunningLog(
    @Json(name = "logId") val logId: Int?,
    @Json(name = "gatheringId") var gatheringId: Int?,
    @Json(name = "runnedDate") val runnedDate: ZonedDateTime,
    @Json(name = "stampCode") val stampCode: String?,
    @Json(name = "isOpened") val isOpened: Int,
)