package kr.devkyu.domain.usecaseImpl.alarm

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 알림 상태 변경
 */
class UpdateAlarmUseCase @Inject constructor(
    private val repo: UserRepository
) {

    suspend operator fun invoke(userId: Int, pushOn : Boolean) : BaseEntity {
        return repo.patchAlarm(userId, pushOn)
    }
}