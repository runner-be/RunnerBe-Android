package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateUserPaceUseCase @Inject constructor(
    private val repo : UserRepository
)  {

    suspend operator fun invoke(userId: Int, pace: String)  : BaseEntity {
        return repo.patchUserPaceRegist(userId, pace)
    }
}