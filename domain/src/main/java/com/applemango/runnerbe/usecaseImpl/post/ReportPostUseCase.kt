package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.PostRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(postId: Int, userId : Int): BaseEntity {
        return repo.reportPost(postId, userId)
    }
}