package com.applemango.domain.usecaseImpl.runninglog

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.RunningLogRepository
import javax.inject.Inject

class DeleteRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(logId: Int): CommonEntity {
        return repository.deleteRunningLog(logId)
    }
}