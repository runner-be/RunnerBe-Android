package kr.devkyu.domain.usecaseImpl.runninglog

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class DeleteRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int): BaseEntity {
        return runningLogRepository.deleteRunningLog(userId, logId)
    }
}