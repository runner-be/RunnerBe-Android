package kr.devkyu.domain.usecaseImpl.post

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 게시글 참여 신청 마감
 */
class ClosePostUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(postId: Int) : BaseEntity {
        return repo.postClosing(postId)
    }
}