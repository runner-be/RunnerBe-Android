package kr.devkyu.domain.usecaseImpl.user

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawalUserUseCase @Inject constructor(private val repo : UserRepository) {

    suspend operator fun invoke(userId: Int, secretKey : String) : BaseEntity {
        return repo.withdrawalUser(userId, secretKey)
    }
}