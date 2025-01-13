package com.applemango.runnerbe.data.dto.new

import com.applemango.runnerbe.entity.RoomInfo
import com.applemango.runnerbe.entity.RunningTalkMessage
import com.squareup.moshi.Json

data class RunningTalkMessagesDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "roomInfo") val roomInfo : List<RoomInfo>,
    @Json(name = "messages") val messages : List<RunningTalkMessage>
)