package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(
        userId: Int, year: Int, month: Int, runningLog: RunningLogRequest
    ): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.postRunningLog(userId, year, month, runningLog)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}