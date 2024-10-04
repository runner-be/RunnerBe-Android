package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteRunningLogApi {

    @DELETE("/runningLogs/{userId}/{logId}")
    suspend fun deleteRunningLog(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int
    ): Response<BaseResponse>
}