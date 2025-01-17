package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteRunningLogApi {

    @DELETE("/runningLogs/{userId}/{logId}")
    suspend fun deleteRunningLog(
        @Path("userId") userId: Int,
        @Path("logId") logId: Int
    ): Response<kr.devkyu.data.dto.CommonDto>
}