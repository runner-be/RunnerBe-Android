package com.applemango.runnerbe.usecaseImpl.alarm

import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.repository.AlarmRepository
import com.applemango.runnerbe.repository.UserRepository
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