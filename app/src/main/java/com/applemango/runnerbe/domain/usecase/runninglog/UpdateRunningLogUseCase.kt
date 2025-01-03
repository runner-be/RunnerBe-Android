package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRunningLogUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, runningLog: RunningLogRequest): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.patchRunningLog(userId, logId, runningLog)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message?:"error"))
        }
    }
}