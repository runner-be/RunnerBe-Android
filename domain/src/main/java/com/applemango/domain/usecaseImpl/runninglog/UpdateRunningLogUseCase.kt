package com.applemango.domain.usecaseImpl.runninglog

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.RunningLogRepository
import javax.inject.Inject

class UpdateRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(logId: Int, runningLog: RunningLogParam): CommonEntity {
        return repository.patchRunningLog(logId, runningLog)
    }

    data class RunningLogParam(
        val runningDate: String,
        val stampCode: String,
        val gatheringId: Int?,
        val contents: String,
        val imageUrl: String?,
        val weatherDegree: Int?,
        val weatherIcon: String?,
        val isOpened: Int
    )
}