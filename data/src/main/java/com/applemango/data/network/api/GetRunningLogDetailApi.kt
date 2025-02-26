package com.applemango.data.network.api

import com.applemango.data.dto.RunningLogDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetRunningLogDetailApi {

    @GET("/runningLogs/{userId}/detail/{logId}")
    suspend fun getRunningLogDetail(
        @Path("userId") targetUserId: Int,
        @Path("logId") logId: Int,
    ): Response<RunningLogDetailDto>
}