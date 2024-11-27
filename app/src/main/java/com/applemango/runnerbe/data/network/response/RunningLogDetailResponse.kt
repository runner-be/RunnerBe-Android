package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class DetailRunningLogResponse(
    val result: DetailRunningLogResult
) : BaseResponse()

data class DetailRunningLogResult(
    @SerializedName("detailRunningLog") val runningLog: DetailRunningLog,
    @SerializedName("gatheringCount") val gatheringCount: Int,
    @SerializedName("gotStamp") val gotStamp: List<MemberStamp>
)

data class DetailRunningLog (
    @SerializedName("status") val status: String,
    @SerializedName("runnedDate") val runnedDate: ZonedDateTime,
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("gatheringId") val gatheringId: Int?,
    @SerializedName("stampCode") val stampCode: String,
    @SerializedName("contents") val contents: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("weatherDegree") val weatherDegree: Int?,
    @SerializedName("weatherIcon") val weatherCode: String?,
    @SerializedName("isOpened") val isOpened: Int,
)

data class MemberStamp (
    @SerializedName("userId") val userId: Int,
    @SerializedName("logId") val logId: Int?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("stampCode") val stampCode: String,
)