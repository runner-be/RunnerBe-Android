package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.RoomDetail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RunningTalkDetailResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : RoomDetail //테스트 필요, array 인지 그냥인지
)
