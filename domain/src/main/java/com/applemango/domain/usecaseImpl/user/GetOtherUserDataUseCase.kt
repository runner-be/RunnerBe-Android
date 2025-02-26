package com.applemango.domain.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.domain.entity.OtherUserEntity
import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class GetOtherUserDataUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(targetUserId: Int): Flow<OtherUserEntity> = flow {
        runCatching {
            repository.getOtherUserProfile(targetUserId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}