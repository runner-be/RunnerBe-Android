package com.applemango.runnerbe.domain.usecase.user

import com.applemango.runnerbe.data.network.request.JoinUserRequest
import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    operator fun invoke(request : JoinUserRequest) : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.joinUser(request)
        }.onSuccess {
            emit(it)
        }.onFailure { e ->
            e.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(e.message))
        }
    }
}