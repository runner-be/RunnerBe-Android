package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam

interface RunningLogRepository {


    suspend fun postRunningLog(userId: Int, year: Int, month: Int, runningLog: RunningLogParam): CommonEntity
    suspend fun patchRunningLog(userId: Int, logId: Int, runningLog: RunningLogParam): CommonEntity
    suspend fun deleteRunningLog(userId: Int, logId: Int): CommonEntity

    suspend fun getRunningLogDetail(userId: Int, logId: Int): RunningLogDetailEntity
    suspend fun getMonthlyRunningLogs(userId: Int, year: Int, month: Int): MonthlyRunningLogEntity
}