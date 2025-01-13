package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, year: Int, month: Int, runningLog: RunningLogParam): CommonEntity {
        return repository.postRunningLog(userId, year, month, runningLog)
    }
}