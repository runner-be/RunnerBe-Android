package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.new.JoinedRunnersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetJoinedRunnersApi {

    @GET("/runningLogs/{userId}/partners/{gatheringId}")
    suspend fun getJoinedRunnerList(
        @Path("userId") userId: Int,
        @Path("gatheringId") gatheringId: Int,
    ): Response<JoinedRunnersResponse>
}