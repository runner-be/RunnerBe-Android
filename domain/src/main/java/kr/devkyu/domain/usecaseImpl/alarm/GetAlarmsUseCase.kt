package kr.devkyu.domain.usecaseImpl.alarm

import kr.devkyu.domain.entity.AlarmEntity
import kr.devkyu.domain.repository.UserRepository
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