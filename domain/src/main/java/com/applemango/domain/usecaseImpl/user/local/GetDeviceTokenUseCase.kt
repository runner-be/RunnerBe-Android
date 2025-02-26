package com.applemango.domain.usecaseImpl.user.local

import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class GetDeviceTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(): String? {
        return repository.getDeviceToken()
    }
}