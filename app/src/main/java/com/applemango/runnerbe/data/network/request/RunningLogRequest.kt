package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class RunningLogRequest(
    @SerializedName("runningDate") val runningDate: String,
    @SerializedName("stampCode") val stampCode: String,
//    @SerializedName("gatheringId") val gatheringId: Int?,
    @SerializedName("contents") val contents: String,
    @SerializedName("weatherDegree") val weatherDegree: Int,
    @SerializedName("weatherIcon") val weatherIcon: String,
    @SerializedName("isOpened") val isOpened: Int
)
