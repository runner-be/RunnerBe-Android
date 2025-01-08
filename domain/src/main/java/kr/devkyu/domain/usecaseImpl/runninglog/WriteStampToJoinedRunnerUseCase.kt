package kr.devkyu.domain.usecaseImpl.runninglog

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningLogRepository
import javax.inject.Inject

class WriteStampToJoinedRunnerUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    suspend operator fun invoke(userId: Int, logId: Int, stamp: PostStampParam): BaseEntity {
        return runningLogRepository.postStampToJoinedRunner(userId, logId, stamp)
    }

    data class PostStampParam(
        val targetUserId: Int,
        val stampCode: String,
    )

}