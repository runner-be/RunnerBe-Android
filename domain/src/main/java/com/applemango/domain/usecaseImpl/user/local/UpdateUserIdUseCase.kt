package com.applemango.domain.usecaseImpl.user.local

import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserIdUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(userId: Int) {
        return repository.updateUserId(userId)
    }
}