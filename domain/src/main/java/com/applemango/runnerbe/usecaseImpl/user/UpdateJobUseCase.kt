package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateJobUseCase @Inject constructor(
    private val repo : UserRepository
) {

    suspend operator fun invoke(userId: Int, job: String) : BaseEntity {
        return repo.jobChange(userId, job)
    }
}