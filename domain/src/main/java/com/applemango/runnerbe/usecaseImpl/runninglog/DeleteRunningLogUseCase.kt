package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import javax.inject.Inject

class DeleteRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(logId: Int): CommonEntity {
        return repository.deleteRunningLog(logId)
    }
}