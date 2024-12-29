package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.Room
import com.squareup.moshi.Json

data class RunningTalksResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : List<Room>
)