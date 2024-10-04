package com.applemango.runnerbe.domain.repository

import com.applemango.runnerbe.data.network.request.PatchStampRequest
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import com.applemango.runnerbe.data.network.response.JoinedRunnerResponse
import com.applemango.runnerbe.data.network.response.MonthlyStampResponse
import com.applemango.runnerbe.presentation.state.CommonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RunningLogRepository {

    suspend fun getMonthlyRunningLogList(userId: Int, year: Int, month: Int): CommonResponse
    suspend fun postRunningLog(userId: Int, year: Int, month: Int, runningLog: RunningLogRequest): CommonResponse
    suspend fun getRunningLogDetail(userId: Int, logId: Int): CommonResponse
    suspend fun patchRunningLog(userId: Int, logId: Int, runningLog: RunningLogRequest): CommonResponse
    suspend fun deleteRunningLog(userId: Int, logId: Int): CommonResponse

    suspend fun getJoinedRunnerList(userId: Int, logId: Int): CommonResponse
    suspend fun getStampList(userId: Int, year: Int, month: Int): CommonResponse // 스탬프 종류 모두 가져오기
    suspend fun patchStampToJoinedRunner(userId: Int, logId: Int, stamp: PatchStampRequest): CommonResponse
    suspend fun postStampToJoinedRunner(userId: Int, logId: Int, stamp: PatchStampRequest): CommonResponse
}