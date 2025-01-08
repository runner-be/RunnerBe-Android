package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.entity.JoinedRunnersEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam

interface RunningLogRepository {

    suspend fun getRunningLogDetail(userId: Int, logId: Int): RunningLogDetailEntity
    suspend fun getMonthlyRunningLogs(userId: Int, year: Int, month: Int): MonthlyRunningLogEntity

    suspend fun postRunningLog(userId: Int, year: Int, month: Int, runningLog: RunningLogParam): BaseEntity
    suspend fun patchRunningLog(userId: Int, logId: Int, runningLog: RunningLogParam): BaseEntity
    suspend fun deleteRunningLog(userId: Int, logId: Int): BaseEntity

    suspend fun getJoinedRunnerList(userId: Int, logId: Int): JoinedRunnersEntity
    suspend fun postStampToJoinedRunner(userId: Int, logId: Int, stamp: PostStampRequest): BaseEntity
}