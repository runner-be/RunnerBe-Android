package com.applemango.runnerbe.domain.repository

import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.presentation.state.CommonResponse

interface RunningLogRepository {

    suspend fun getMonthlyRunningLogList(userId: Int, year: Int, month: Int): CommonResponse
    suspend fun postRunningLog(userId: Int, year: Int, month: Int, runningLog: RunningLogRequest): CommonResponse
    suspend fun getRunningLogDetail(userId: Int, logId: Int): CommonResponse
    suspend fun patchRunningLog(userId: Int, logId: Int, runningLog: RunningLogRequest): CommonResponse
    suspend fun deleteRunningLog(userId: Int, logId: Int): CommonResponse

    suspend fun getJoinedRunnerList(userId: Int, logId: Int): CommonResponse
    suspend fun getStampList(userId: Int, year: Int, month: Int): CommonResponse // 스탬프 종류 모두 가져오기
    suspend fun patchStampToJoinedRunner(userId: Int, logId: Int, stamp: PostStampRequest): CommonResponse
    suspend fun postStampToJoinedRunner(userId: Int, logId: Int, stamp: PostStampRequest): CommonResponse
}