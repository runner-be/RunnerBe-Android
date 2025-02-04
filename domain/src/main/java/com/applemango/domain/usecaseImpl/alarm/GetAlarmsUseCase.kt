package com.applemango.domain.usecaseImpl.alarm

import com.applemango.domain.entity.AlarmEntity
import com.applemango.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 알림 목록 조회
 */
class GetAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<AlarmEntity>> {
        return repository.getAlarms()
    }
}