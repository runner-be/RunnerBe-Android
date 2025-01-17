package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(params : JoinUserParam) : NewUserEntity {
        return repository.joinUser(
            params.uuid,
            params.nickName,
            params.birthday,
            params.genderTag,
            params.jobTag,
            params.deviceToken
        )
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