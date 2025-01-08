package com.applemango.runnerbe.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.repository.UserRepository
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