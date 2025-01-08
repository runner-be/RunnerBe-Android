package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.PostRepository
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