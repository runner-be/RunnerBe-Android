package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

// TODO
data class MonthlyStampResponse(
    val result: RunningLogResult
) : BaseResponse()

data class RunningLogResult (
    @SerializedName("totalCount") val totalCount: TotalCount?,
    @SerializedName("myRunningLog") val runningLog: List<RunningLog>,
)

data class TotalCount(
    @SerializedName("groupRunningCount") val groupRunningCount: Int,
    @SerializedName("personalRunningCount") val personalRunningCount: Int
)

data class RunningLog(
    @SerializedName("logId") val logId: Int,
    @SerializedName("gatheringId") val gatheringId: Int?,
    @SerializedName("runnedDate") val runnedDate: ZonedDateTime,
    @SerializedName("stampCode") val stampCode: String
)