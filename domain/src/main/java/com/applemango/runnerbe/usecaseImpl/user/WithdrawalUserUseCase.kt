package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class WithdrawalUserUseCase @Inject constructor(
    private val repository : UserRepository,
) {

    suspend operator fun invoke(secretKey : String) : CommonEntity {
        return repository.withdrawalUser(secretKey)
    }
}