package com.applemango.runnerbe.usecaseImpl.alarm

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.UserRepository
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