package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.MessageReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostMessageReportApi {

    @POST("/messages/report")
    suspend fun messageReport(
        @Body request : MessageReportRequest
    ) : Response<CommonDto>
}