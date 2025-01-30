package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(year: Int, month: Int, runningLog: RunningLogParam): CommonEntity {
        return repository.postRunningLog(year, month, runningLog)
    }
}