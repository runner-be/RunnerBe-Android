package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.PostingRepository
import javax.inject.Inject

/**
 * 게시글 참여 신청 마감
 */
class ClosePostUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(postId: Int) : CommonEntity {
        return repository.postClosing(postId)
    }
}