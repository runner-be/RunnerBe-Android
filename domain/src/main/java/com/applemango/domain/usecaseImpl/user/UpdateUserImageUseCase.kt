package com.applemango.domain.usecaseImpl.user

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(imageUrl : String?) : CommonEntity {
        return repository.patchUserImage(imageUrl)
    }
}