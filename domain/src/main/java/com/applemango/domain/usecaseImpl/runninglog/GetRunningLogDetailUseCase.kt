package com.applemango.domain.usecaseImpl.runninglog

import com.applemango.domain.entity.RunningLogDetailEntity
import com.applemango.domain.repository.RunningLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunningLogDetailUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(targetUserId: Int, logId: Int): Flow<RunningLogDetailEntity> = flow {
        emit(runningLogRepository.getRunningLogDetail(targetUserId, logId))
    }
}