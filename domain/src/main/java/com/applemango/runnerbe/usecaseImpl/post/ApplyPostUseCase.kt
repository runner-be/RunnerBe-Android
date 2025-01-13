package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.PostingRepository
import javax.inject.Inject

/**
 * 게시글에 참여신청
 */
class ApplyPostUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(postId: Int, userId : Int) : CommonEntity {
        return repository.postApply(postId, userId)
    }
}