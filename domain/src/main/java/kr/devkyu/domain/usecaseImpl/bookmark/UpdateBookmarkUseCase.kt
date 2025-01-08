package kr.devkyu.domain.usecaseImpl.bookmark

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 특정 게시글 북마크 상태 변경
 */
class UpdateBookmarkUseCase @Inject constructor(
    private val repo : UserRepository
) {
    suspend operator fun invoke(userId : Int, postId : Int, whetherAdd: String) : BaseEntity {
        return repo.bookMarkStatusChange(userId, postId, whetherAdd)
    }
}