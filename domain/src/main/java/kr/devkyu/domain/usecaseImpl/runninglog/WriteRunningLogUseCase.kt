package kr.devkyu.domain.usecaseImpl.runninglog

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningLogRepository
import kr.devkyu.domain.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, year: Int, month: Int, runningLog: RunningLogParam): BaseEntity {
        return runningLogRepository.postRunningLog(userId, year, month, runningLog)
    }
}