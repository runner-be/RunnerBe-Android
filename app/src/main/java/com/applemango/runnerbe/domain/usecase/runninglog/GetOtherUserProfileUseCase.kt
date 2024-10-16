package com.applemango.runnerbe.domain.usecase.runninglog

import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOtherUserProfileUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(targetUserId: Int): Flow<CommonResponse> = flow {
        runCatching {
            runningLogRepository.getOtherUserProfile(targetUserId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}