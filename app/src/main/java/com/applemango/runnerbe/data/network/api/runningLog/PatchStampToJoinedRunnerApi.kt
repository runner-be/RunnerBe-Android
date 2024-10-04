package com.applemango.runnerbe.data.network.api.runningLog

import com.applemango.runnerbe.data.network.request.PatchStampRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchStampToJoinedRunnerApi {

    @PATCH("runningLogs/{userId}/partners/{logId}")
    suspend fun patchStampToJoinedRunner(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int,
        @Body stamp: PatchStampRequest
    ): Response<BaseResponse>
}