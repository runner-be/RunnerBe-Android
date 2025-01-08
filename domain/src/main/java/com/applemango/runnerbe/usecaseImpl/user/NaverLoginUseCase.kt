package com.applemango.runnerbe.usecaseImpl.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class NaverLoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(accessToken: String): Flow<SocialLoginEntity> = flow {
        kotlin.runCatching {
            repository.naverLogin(accessToken)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)
}