package com.applemango.runnerbe.domain.usecase.alarm

import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 알림 상태 변경
 */
class UpdateAlarmUseCase @Inject constructor(private val repo: UserRepository) {

    operator fun invoke(userId: Int, pushOn : Boolean) : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.patchAlarm(userId, pushOn)
        }.onSuccess {
            emit(it)
        }.onFailure { e ->
            e.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(e.message))
        }
    }
}