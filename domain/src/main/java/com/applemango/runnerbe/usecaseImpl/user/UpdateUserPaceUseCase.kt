package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateUserPaceUseCase @Inject constructor(
    private val repository : UserRepository
)  {

    suspend operator fun invoke(userId: Int, pace: String)  : CommonEntity {
        return repository.patchUserPaceRegist(userId, pace)
    }
}