package com.applemango.runnerbe.usecaseImpl.user.local

import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateJwtTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(token: String) {
        return repository.updateJwtToken(token)
    }
}