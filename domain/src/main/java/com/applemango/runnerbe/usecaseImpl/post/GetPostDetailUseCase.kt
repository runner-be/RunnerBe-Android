package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repository: PostingRepository
) {

    suspend operator fun invoke(postId: Int, userId: Int): Flow<PostingDetailEntity> = flow {
        repository.getPostDetail(postId, userId)
    }
}

data class PostDetailManufacture(
    val code: Int,
    val post: PostingEntity,
    val runnerInfo: List<UserEntity>?,
    val waitingInfo: List<UserEntity>?,
    val roomId: Int
)