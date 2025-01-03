package com.applemango.runnerbe.domain.usecase.alarm

import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 알림 목록 조회
 */
class GetAlarmsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            userRepository.getNotifications()
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}