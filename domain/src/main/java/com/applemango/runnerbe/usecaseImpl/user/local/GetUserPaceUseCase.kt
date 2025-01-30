package com.applemango.runnerbe.usecaseImpl.user.local

import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class GetUserPaceUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(): String {
        return repository.getUserPace()
    }
}