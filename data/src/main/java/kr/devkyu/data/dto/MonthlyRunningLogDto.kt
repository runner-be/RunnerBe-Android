package kr.devkyu.data.dto

import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class MonthlyRunningLogDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: kr.devkyu.data.dto.MonthlyRunningLog
)

data class MonthlyRunningLog(
    @Json(name = "totalCount") val totalCount: kr.devkyu.data.dto.TotalCount?,
    @Json(name = "myRunningLog") val runningLog: List<kr.devkyu.data.dto.RunningLogDto>,
    @Json(name = "isExistGathering") val gatheringDays: List<kr.devkyu.data.dto.GatheringData>,
)

data class TotalCount(
    @Json(name = "groupRunningCount") val groupRunningCount: Int,
    @Json(name = "personalRunningCount") val personalRunningCount: Int
)

data class GatheringData (
    @Json(name = "gatheringId") val gatheringId: Int,
    @Json(name = "gatheringTime") val date: ZonedDateTime
)