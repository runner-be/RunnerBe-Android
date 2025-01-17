package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.MonthlyRunningLogDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMonthlyRunningLogsApi {

    @GET("/runningLogs/{userId}")
    suspend fun getMonthlyRunningLog(
        @Path("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<MonthlyRunningLogDto>
}