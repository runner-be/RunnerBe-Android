package com.applemango.runnerbe.usecaseImpl.user

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(imageUrl : String?) : BaseEntity {
        return repo.patchUserImage(imageUrl)
    }
}