package com.applemango.runnerbe.domain.usecase.bookmark

import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBeforeBookmarkListUseCase @Inject constructor(private val repo: PostRepository) {
    operator fun invoke(userId: Int): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.getBookmarkList(userId)
        }.onSuccess {
            if(it is CommonResponse.Success<*> && it.body is GetBookmarkResponse && it.body.result.bookMarkList != null) {
                val result = it.body.result.bookMarkList!!.filter { post -> post.runningTag == RunningTag.Before.tag }
                it.body.result.bookMarkList = result
            }
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}