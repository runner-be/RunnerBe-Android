package com.applemango.runnerbe.usecaseImpl.alarm

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.AlarmRepository
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

/**
 * 알림 상태 변경
 */
class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(userId: Int, pushOn : Boolean) : CommonEntity {
        return repository.patchAlarm(userId, pushOn)
    }
}