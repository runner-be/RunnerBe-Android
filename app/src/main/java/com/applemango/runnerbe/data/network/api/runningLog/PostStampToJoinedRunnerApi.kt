package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostStampToJoinedRunnerApi {

    @POST("runningLogs/{userId}/partners/{logId}")
    suspend fun postStampToJoinedRunner(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int,
        @Body stamp: PostStampRequest
    ): Response<BaseResponse>
}