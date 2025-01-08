package kr.devkyu.domain.usecaseImpl.runninglog

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class UpdateRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, runningLog: RunningLogParam): BaseEntity {
        return runningLogRepository.patchRunningLog(userId, logId, runningLog)
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