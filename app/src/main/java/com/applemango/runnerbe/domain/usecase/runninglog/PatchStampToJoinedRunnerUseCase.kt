package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatchStampToJoinedRunnerUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, stamp: PostStampRequest): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.patchStampToJoinedRunner(userId, logId, stamp)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}