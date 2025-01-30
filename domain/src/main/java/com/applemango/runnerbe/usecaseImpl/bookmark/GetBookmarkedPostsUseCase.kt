package com.applemango.runnerbe.usecaseImpl.bookmark

import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 북마크 게시글 목록 조회
 */
class GetBookmarkedPostsUseCase @Inject constructor(
    private val repo: PostingRepository
) {
    suspend operator fun invoke(): Flow<List<PostingEntity>> = flow {
        repo.getBookmarkList()
    }
}