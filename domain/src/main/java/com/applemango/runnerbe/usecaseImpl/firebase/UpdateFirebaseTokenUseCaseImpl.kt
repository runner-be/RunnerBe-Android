package com.applemango.runnerbe.usecaseImpl.firebase

import com.applemango.runnerbe.repository.FirebaseTokenRepository
import javax.inject.Inject

class UpdateFirebaseTokenUseCaseImpl @Inject constructor(
    private val repository: FirebaseTokenRepository
) {

    suspend operator fun invoke(userId: Int, token: String) {
        if (userId != -1) {
            repository.updateToken(userId, token)
        }
    }
}