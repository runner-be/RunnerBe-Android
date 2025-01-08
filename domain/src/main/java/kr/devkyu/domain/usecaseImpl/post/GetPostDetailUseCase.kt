package kr.devkyu.domain.usecaseImpl.post

import kr.devkyu.domain.entity.PostingEntity
import kr.devkyu.domain.entity.UserEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repo: PostRepository
) {

    suspend operator fun invoke(postId: Int, userId: Int): PostingEntity {
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