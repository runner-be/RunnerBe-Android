package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.JoinedRunnerRepository
import javax.inject.Inject

/**
 * 참여 신청한 러너의 참여를 승인/거부
 */
class AcceptOrDenyRunnerUseCase @Inject constructor(
    private val repo: JoinedRunnerRepository
) {

    suspend operator fun invoke(postId : Int, applicantId: Int, whetherAccept : String) : CommonEntity {
        return repo.postWhetherAccept(postId, applicantId, whetherAccept)
    }
}