package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.repository.FirebaseTokenRepository
import javax.inject.Inject

class UpdateFirebaseTokenUseCase @Inject constructor(
    private val repository: FirebaseTokenRepository
) {

    suspend operator fun invoke() {
        repository.updateFirebaseToken()
    }
}