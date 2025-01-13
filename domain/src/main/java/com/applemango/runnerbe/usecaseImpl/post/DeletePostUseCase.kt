package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.PostingRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(postId: Int, userId: Int) : CommonEntity {
        return repository.dropPost(postId, userId)
    }
}