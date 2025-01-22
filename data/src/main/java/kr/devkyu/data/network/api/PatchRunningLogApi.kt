package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import kr.devkyu.data.network.request.RunningLogRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchRunningLogApi {

    @PATCH("/runningLogs/{userId}/{logId}")
    suspend fun patchRunningLog(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int,
        @Body runningLog: RunningLogRequest
    ): Response<CommonDto>
}