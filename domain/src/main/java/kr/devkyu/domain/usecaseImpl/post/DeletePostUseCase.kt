package kr.devkyu.domain.usecaseImpl.post

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(postId: Int, userId: Int) : BaseEntity {
        return repo.dropPost(postId, userId)
    }
}