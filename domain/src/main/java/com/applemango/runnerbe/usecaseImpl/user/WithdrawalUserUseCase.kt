package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class WithdrawalUserUseCase @Inject constructor(private val repo : UserRepository) {

    suspend operator fun invoke(userId: Int, secretKey : String) : CommonEntity {
        return repo.withdrawalUser(userId, secretKey)
    }
}