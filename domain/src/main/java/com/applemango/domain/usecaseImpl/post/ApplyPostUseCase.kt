package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.PostingRepository
import javax.inject.Inject

/**
 * 게시글에 참여신청
 */
class ApplyPostUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(postId: Int) : CommonEntity {
        return repository.postApply(postId)
    }
}