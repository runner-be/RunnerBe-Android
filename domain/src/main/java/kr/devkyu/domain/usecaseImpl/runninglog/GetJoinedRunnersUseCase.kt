package kr.devkyu.domain.usecaseImpl.runninglog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.JoinedRunnersEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class GetJoinedRunnersUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    operator fun invoke(userId: Int, gatheringId: Int): Flow<JoinedRunnersEntity> = flow {
        kotlin.runCatching {
            runningLogRepository.getJoinedRunnerList(userId, gatheringId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}