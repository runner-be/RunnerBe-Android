package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.MessageReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostMessageReportApi {

    @POST("/messages/report")
    suspend fun messageReport(@Body request : MessageReportRequest) : Response<kr.devkyu.data.dto.CommonDto>
}