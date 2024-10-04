package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DropPostUseCase @Inject constructor(private val repo: PostRepository) {
    operator fun invoke(postId: Int, userId: Int) :Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.dropPost(postId, userId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}