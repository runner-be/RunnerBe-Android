package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.MyPageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository : UserRepository
) {

    operator fun invoke() :Flow<MyPageEntity> = flow {
        runCatching {
            repository.getUserData()
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}