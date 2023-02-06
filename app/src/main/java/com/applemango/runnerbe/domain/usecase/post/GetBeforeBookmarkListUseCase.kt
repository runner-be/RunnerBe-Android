package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.repository.PostRepository
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
            if(it is CommonResponse.Success<*> && it.body is GetBookmarkResponse) {
                val result = it.body.result.bookMarkList.filter { post -> post.runningTag == "B" }
                it.body.result.bookMarkList = result
            }
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}