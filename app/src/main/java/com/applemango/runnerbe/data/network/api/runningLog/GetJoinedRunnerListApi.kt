package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.response.JoinedRunnerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetJoinedRunnerListApi {

    @GET("/runningLogs/{userId}/partners/{gatheringId}")
    suspend fun getJoinedRunnerList(
        @Path("userId") userId: Int,
        @Path("gatheringId") gatheringId: Int,
    ): Response<JoinedRunnerResponse>
}