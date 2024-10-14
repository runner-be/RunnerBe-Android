package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteRunningUseCase @Inject constructor(private val repo: PostRepository) {

    operator fun invoke(userId : Int, body : WriteRunningRequest) : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.writeRunning(userId, body)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}