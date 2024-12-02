package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class MonthlyStampResponse(
    val result: RunningLogResult
) : BaseResponse()

data class RunningLogResult (
    @SerializedName("totalCount") val totalCount: TotalCount?,
    @SerializedName("myRunningLog") val runningLog: List<RunningLog>,
    @SerializedName("isExistGathering") val gatheringDays: List<GatheringData>,
)

data class GatheringData (
    @SerializedName("gatheringId") val gatheringId: Int,
    @SerializedName("gatheringTime") val date: ZonedDateTime
)

data class TotalCount(
    @SerializedName("groupRunningCount") val groupRunningCount: Int,
    @SerializedName("personalRunningCount") val personalRunningCount: Int
)

data class RunningLog(
    @SerializedName("logId") val logId: Int?,
    @SerializedName("gatheringId") var gatheringId: Int?,
    @SerializedName("runnedDate") val runnedDate: ZonedDateTime,
    @SerializedName("stampCode") val stampCode: String?,
    @SerializedName("isOpened") val isOpened: Int,
)