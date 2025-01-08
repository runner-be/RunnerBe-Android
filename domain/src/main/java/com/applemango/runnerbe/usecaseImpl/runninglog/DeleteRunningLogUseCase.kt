package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import javax.inject.Inject

class DeleteRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int): BaseEntity {
        return runningLogRepository.deleteRunningLog(userId, logId)
    }
}