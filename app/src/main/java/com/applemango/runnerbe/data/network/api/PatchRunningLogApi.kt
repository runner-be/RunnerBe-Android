package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface PatchRunningLogApi {

    @PATCH("/runningLogs/{userId}/{logId}")
    suspend fun patchRunningLog(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int,
        @Body runningLog: RunningLogRequest
    ): Response<BaseResponse>
}