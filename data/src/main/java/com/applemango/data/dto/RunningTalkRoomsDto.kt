package com.applemango.data.dto

import com.squareup.moshi.Json

data class RunningTalkRoomsDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : List<RunningTalkRoomDto>
)