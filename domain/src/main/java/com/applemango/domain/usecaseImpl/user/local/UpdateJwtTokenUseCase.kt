package com.applemango.domain.usecaseImpl.user.local

import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class UpdateJwtTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(token: String) {
        return repository.updateJwtToken(token)
    }
}