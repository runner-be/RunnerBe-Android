package kr.devkyu.domain.usecaseImpl.user

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val repo : UserRepository
) {

    suspend operator fun invoke(userId: Int, nickname : String) : BaseEntity {
        return repo.nicknameChange(userId, nickname)
    }
}