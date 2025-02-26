package com.applemango.domain.usecaseImpl.user

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawalUserUseCase @Inject constructor(
    private val repository : UserRepository,
) {

    suspend operator fun invoke(secretKey : String) : CommonEntity {
        return repository.withdrawalUser(secretKey)
    }
}