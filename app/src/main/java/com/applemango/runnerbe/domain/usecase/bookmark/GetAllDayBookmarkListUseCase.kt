package com.applemango.runnerbe.domain.usecase.bookmark

import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllDayBookmarkListUseCase @Inject constructor(private val repo: PostRepository) {
    operator fun invoke(userId: Int): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.getBookmarkList(userId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}