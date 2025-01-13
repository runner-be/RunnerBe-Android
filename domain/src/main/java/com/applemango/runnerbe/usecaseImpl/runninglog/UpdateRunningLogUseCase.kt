package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import javax.inject.Inject

class UpdateRunningLogUseCase @Inject constructor(
    private val repository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, runningLog: RunningLogParam): CommonEntity {
        return repository.patchRunningLog(userId, logId, runningLog)
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