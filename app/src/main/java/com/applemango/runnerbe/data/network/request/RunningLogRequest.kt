package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class RunningLogRequest(
    @Json(name = "runningDate") val runningDate: String,
    @Json(name = "stampCode") val stampCode: String,
    @Json(name = "gatheringId") val gatheringId: Int?,
    @Json(name = "contents") val contents: String,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "weatherDegree") val weatherDegree: Int?,
    @Json(name = "weatherIcon") val weatherIcon: String?,
    @Json(name = "isOpened") val isOpened: Int
)
