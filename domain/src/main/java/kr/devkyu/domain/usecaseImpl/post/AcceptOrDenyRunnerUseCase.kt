package kr.devkyu.domain.usecaseImpl.post

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 참여 신청한 러너의 참여를 승인/거부
 */
class AcceptOrDenyRunnerUseCase @Inject constructor(
    private val repo: PostRepository
) {

    suspend operator fun invoke(postId : Int, applicantId: Int, whetherAccept : String) : BaseEntity {
        return repo.postWhetherAccept(postId, applicantId, whetherAccept)
    }
}