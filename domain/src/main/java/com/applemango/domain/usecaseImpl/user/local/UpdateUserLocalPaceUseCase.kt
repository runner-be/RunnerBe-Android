package com.applemango.domain.usecaseImpl.user.local

import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserLocalPaceUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(pace: String) {
        repository.updateUserPace(pace)
    }
}