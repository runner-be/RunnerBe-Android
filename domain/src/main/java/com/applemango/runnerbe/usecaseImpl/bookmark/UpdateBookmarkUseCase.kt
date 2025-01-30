package com.applemango.runnerbe.usecaseImpl.bookmark

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.UserRepository
import javax.inject.Inject

/**
 * 특정 게시글 북마크 상태 변경
 */
class UpdateBookmarkUseCase @Inject constructor(
    private val repository : UserRepository
) {
    suspend operator fun invoke(postId : Int, whetherAdd: String) : CommonEntity {
        return repository.bookMarkStatusChange(postId, whetherAdd)
    }
}