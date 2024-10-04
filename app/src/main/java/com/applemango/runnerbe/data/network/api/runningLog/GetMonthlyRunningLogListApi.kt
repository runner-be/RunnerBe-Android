package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.response.MonthlyStampResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMonthlyRunningLogListApi {

    @GET("/runningLogs/{userId}")
    suspend fun getMonthlyRunningLog(
        @Path("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<MonthlyStampResponse>
}