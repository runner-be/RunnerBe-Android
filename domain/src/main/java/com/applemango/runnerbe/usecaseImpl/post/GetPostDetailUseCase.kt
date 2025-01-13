package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.repository.PostingRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repo: PostingRepository
) {

    suspend operator fun invoke(postId: Int, userId: Int): PostingDetailEntity {
        return repo.getPostDetail(postId, userId)
    }
}

data class PostDetailManufacture(
    val code: Int,
    val post: PostingEntity,
    val runnerInfo: List<UserEntity>?,
    val waitingInfo: List<UserEntity>?,
    val roomId: Int
)