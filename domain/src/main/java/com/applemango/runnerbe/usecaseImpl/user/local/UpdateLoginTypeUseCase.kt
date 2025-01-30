package com.applemango.runnerbe.usecaseImpl.user.local

import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateLoginTypeUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(loginType: Int) {
        repository.updateLoginType(loginType)
    }
}