package kr.devkyu.domain.usecaseImpl.runninglog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.MonthlyRunningLogEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class GetMonthlyRunningLogsUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    operator fun invoke(userId: Int, year: Int, month: Int): Flow<MonthlyRunningLogEntity> = flow {
        kotlin.runCatching {
            runningLogRepository.getMonthlyRunningLogs(userId, year, month)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}