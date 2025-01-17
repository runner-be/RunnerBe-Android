package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.MyPageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repo : UserRepository
) {

    operator fun invoke(userId: Int) :Flow<MyPageEntity> = flow {
        runCatching {
            repo.getUserData(userId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}