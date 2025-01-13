package com.applemango.runnerbe.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(params : JoinUserParam) : Flow<NewUserEntity> = flow {
        runCatching {
            repository.joinUser(
                params.uuid,
                params.nickName,
                params.birthday,
                params.genderTag,
                params.jobTag,
                params.deviceToken
            )
        }.onSuccess {
            emit(it)
        }.onFailure { e ->
            e.printStackTrace()
        }
    }

    data class JoinUserParam(
        val uuid : String,
        val nickName : String? = null,
        val birthday: Int,
        val genderTag : String,
        val jobTag : String,
        val deviceToken : String
    )
}