package com.applemango.runnerbe.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class DetailRunningLogResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: DetailRunningLogResult
)

@JsonClass(generateAdapter = true)
data class DetailRunningLogResult(
    @Json(name = "detailRunningLog") val runningLog: DetailRunningLog,
    @Json(name = "gatheringCount") val gatheringCount: Int,
    @Json(name = "gotStamp") val gotStamp: List<MemberStamp>
)

@JsonClass(generateAdapter = true)
data class DetailRunningLog (
    @Json(name = "status") val status: String,
    @Json(name = "runnedDate") val runnedDate: ZonedDateTime,
    @Json(name = "userId") val userId: Int,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "gatheringId") val gatheringId: Int?,
    @Json(name = "stampCode") val stampCode: String,
    @Json(name = "contents") val contents: String,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "weatherDegree") val weatherDegree: Int?,
    @Json(name = "weatherIcon") val weatherCode: String?,
    @Json(name = "isOpened") val isOpened: Int,
)

@JsonClass(generateAdapter = true)
data class MemberStamp (
    @Json(name = "userId") val userId: Int,
    @Json(name = "logId") val logId: Int?,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    @Json(name = "stampCode") val stampCode: String,
)