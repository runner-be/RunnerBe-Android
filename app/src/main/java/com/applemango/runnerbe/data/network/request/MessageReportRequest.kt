package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class MessageReportRequest(
    @Json(name = "messageIdList") val messageIdList : String //1,2,3 형태로 작성
)
