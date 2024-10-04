package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.data.network.request.PatchStampRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatchStampToJoinedRunnerUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, stamp: PatchStampRequest): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.patchStampToJoinedRunner(userId, logId, stamp)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message?:"error"))
        }
    }
}