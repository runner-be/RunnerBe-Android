package com.applemango.runnerbe.domain.usecase.bookmark

import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 특정 게시글 북마크 상태 변경
 */
class UpdateBookmarkUseCase @Inject constructor(
    private val repo : UserRepository
) {
    operator fun invoke(userId : Int, postId : Int, whetherAdd: String) : Flow<CommonResponse> = flow {
        runCatching {
            repo.bookMarkStatusChange(userId, postId, whetherAdd)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}