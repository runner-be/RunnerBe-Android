package com.applemango.runnerbe.usecaseImpl.bookmark

import com.applemango.runnerbe.entity.BookmarksEntity
import com.applemango.runnerbe.repository.PostRepository
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