package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetRunningLogDetailApi {

    @GET("/runningLogs/{userId}/detail/{logId}")
    suspend fun getRunningLogDetail(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int,
    ): Response<DetailRunningLogResponse>
}