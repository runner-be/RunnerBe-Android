package kr.devkyu.domain.usecaseImpl.user

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(imageUrl : String?) : BaseEntity {
        return repo.patchUserImage(imageUrl)
    }
}