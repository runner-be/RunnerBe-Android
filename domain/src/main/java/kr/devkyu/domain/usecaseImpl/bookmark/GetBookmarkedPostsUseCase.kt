package kr.devkyu.domain.usecaseImpl.bookmark

import kr.devkyu.domain.entity.BookmarksEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 북마크 게시글 목록 조회
 */
class GetBookmarkedPostsUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(userId: Int): BookmarksEntity {
        return repo.getBookmarkList(userId)
    }
}