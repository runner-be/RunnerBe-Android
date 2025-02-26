package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.WriteRunningRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostRunningApi {

    @POST("postings/{userId}")
    suspend fun writingRunning(
        @Path("userId") userId: Int,
        @Body body: WriteRunningRequest
    ) : Response<CommonDto>
}