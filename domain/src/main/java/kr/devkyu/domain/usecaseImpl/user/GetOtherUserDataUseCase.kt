package kr.devkyu.domain.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.OtherUserEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class GetOtherUserDataUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(targetUserId: Int): Flow<OtherUserEntity> = flow {
        runCatching {
            repo.getOtherUserProfile(targetUserId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}