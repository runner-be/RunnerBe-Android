package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Query

interface PatchRunningLogApi {

    @PATCH("/runningLogs")
    suspend fun patchRunningLog(
        @Query("userId") userId: Int,
        @Query("logId") logId: Int,
        @Body runningLog: RunningLogRequest
    ): Response<BaseResponse>
}