package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.PostStampRequest
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
    ): Response<CommonDto>
}