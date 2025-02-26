package com.applemango.domain.repository

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.entity.RunningLogDetailEntity
import com.applemango.domain.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam

interface RunningLogRepository {

    suspend fun postRunningLog(year: Int, month: Int, runningLog: RunningLogParam): CommonEntity
    suspend fun patchRunningLog(logId: Int, runningLog: RunningLogParam): CommonEntity
    suspend fun deleteRunningLog(logId: Int): CommonEntity

    suspend fun getRunningLogDetail(targetUserId: Int, logId: Int): RunningLogDetailEntity
    suspend fun getMonthlyRunningLogs(targetUserId: Int, year: Int, month: Int): MonthlyRunningLogEntity
}