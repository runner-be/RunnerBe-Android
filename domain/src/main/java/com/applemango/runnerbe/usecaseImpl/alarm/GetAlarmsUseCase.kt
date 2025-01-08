package com.applemango.runnerbe.usecaseImpl.alarm

import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

/**
 * 알림 목록 조회
 */
class GetAlarmsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): AlarmEntity {
        return userRepository.getAlarms()
    }
}