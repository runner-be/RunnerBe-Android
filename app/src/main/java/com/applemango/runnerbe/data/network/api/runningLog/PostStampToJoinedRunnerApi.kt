package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostStampToJoinedRunnerApi {

    @POST("runningLogs/{userId}/partners/{gatheringId}")
    suspend fun postStampToJoinedRunner(
        @Path("userId") userId: Int,
        @Path("gatheringId") gatheringId: Int,
        @Body stamp: PostStampRequest
    ): Response<BaseResponse>
}