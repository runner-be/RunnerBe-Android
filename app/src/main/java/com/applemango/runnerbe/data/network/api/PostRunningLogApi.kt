package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRunningLogApi {

    @POST("/runningLogs/{userId}")
    suspend fun postRunningLog(
        @Path("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Body runningLog: RunningLogRequest
    ): Response<BaseResponse>
}