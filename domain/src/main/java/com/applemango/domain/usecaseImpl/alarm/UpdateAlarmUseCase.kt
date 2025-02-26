package com.applemango.domain.usecaseImpl.alarm

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.AlarmRepository
import javax.inject.Inject

/**
 * 알림 상태 변경
 */
class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(pushOn : Boolean) : CommonEntity {
        return repository.patchAlarm(pushOn)
    }
}