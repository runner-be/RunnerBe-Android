package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import javax.inject.Inject

class GetRunningLogDetailUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int): RunningLogDetailEntity {
        return runningLogRepository.getRunningLogDetail(userId, logId)
    }
}