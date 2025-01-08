package kr.devkyu.domain.usecaseImpl.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.devkyu.domain.entity.SocialLoginEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(accessToken: String): Flow<SocialLoginEntity> = flow {
        kotlin.runCatching {
            repository.kakaoLogin(accessToken)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)
}