package kr.devkyu.domain.usecaseImpl.runninglog

import kr.devkyu.domain.entity.RunningLogDetailEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class GetRunningLogDetailUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int): RunningLogDetailEntity {
        return runningLogRepository.getRunningLogDetail(userId, logId)
    }
}