package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.new.CommonDto
import com.applemango.runnerbe.data.network.request.MessageReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostMessageReportApi {

    @POST("/messages/report")
    suspend fun messageReport(@Body request : MessageReportRequest) : Response<CommonDto>
}