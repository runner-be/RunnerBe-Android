package com.applemango.domain.usecaseImpl.runninglog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.repository.RunningLogRepository
import javax.inject.Inject

class GetMonthlyRunningLogsUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    operator fun invoke(targetUserId: Int, year: Int, month: Int): Flow<MonthlyRunningLogEntity> = flow {
        kotlin.runCatching {
            runningLogRepository.getMonthlyRunningLogs(targetUserId, year, month)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}