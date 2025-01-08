package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, year: Int, month: Int, runningLog: RunningLogParam): BaseEntity {
        return runningLogRepository.postRunningLog(userId, year, month, runningLog)
    }
}