package com.applemango.domain.usecaseImpl.user

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.UserRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val repository : UserRepository
) {

    suspend operator fun invoke(nickname : String) : CommonEntity {
        return repository.nicknameChange(nickname)
    }
}