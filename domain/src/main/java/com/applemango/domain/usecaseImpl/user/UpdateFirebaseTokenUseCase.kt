package com.applemango.domain.usecaseImpl.user

import com.applemango.domain.repository.FirebaseTokenRepository
import javax.inject.Inject

class UpdateFirebaseTokenUseCase @Inject constructor(
    private val repository: FirebaseTokenRepository
) {

    suspend operator fun invoke() {
        repository.updateFirebaseToken()
    }
}