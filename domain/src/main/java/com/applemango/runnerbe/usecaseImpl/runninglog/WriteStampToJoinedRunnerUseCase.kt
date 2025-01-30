package com.applemango.runnerbe.usecaseImpl.runninglog

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.JoinedRunnerRepository
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