package kr.devkyu.domain.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.UserEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repo : UserRepository
) {

    operator fun invoke(userId: Int) :Flow<UserEntity> = flow {
        runCatching {
            repo.getUserData(userId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}