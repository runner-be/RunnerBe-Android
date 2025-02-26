package com.applemango.domain.usecaseImpl.runninglog

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.JoinedRunnerRepository
import javax.inject.Inject

class WriteStampToJoinedRunnerUseCase @Inject constructor(
    private val repository: JoinedRunnerRepository
) {
    suspend operator fun invoke(logId: Int, stamp: PostStampParam): CommonEntity {
        return repository.postStampToJoinedRunner(logId, stamp)
    }

    data class PostStampParam(
        val targetUserId: Int,
        val stampCode: String,
    )

}