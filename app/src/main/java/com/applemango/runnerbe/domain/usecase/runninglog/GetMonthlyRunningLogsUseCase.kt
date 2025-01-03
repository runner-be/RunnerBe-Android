package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMonthlyRunningLogsUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, year: Int, month: Int): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.getMonthlyRunningLogList(userId, year, month)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}