package com.applemango.runnerbe.usecaseImpl.user.local

import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateUserLocalPaceUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(pace: String) {
        repository.updateUserPace(pace)
    }
}