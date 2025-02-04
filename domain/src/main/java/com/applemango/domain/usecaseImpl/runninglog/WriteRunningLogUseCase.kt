package com.applemango.domain.usecaseImpl.runninglog

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.RunningLogRepository
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(year: Int, month: Int, runningLog: UpdateRunningLogUseCase.RunningLogParam): CommonEntity {
        return repository.postRunningLog(year, month, runningLog)
    }
}