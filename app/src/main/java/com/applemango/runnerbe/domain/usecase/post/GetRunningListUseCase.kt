package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.network.request.GetRunningListRequest
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunningListUseCase @Inject constructor(private val repo: PostRepository) {
    operator fun invoke(runningTag: RunningTag, request : GetRunningListRequest) : Flow<CommonResponse> = flow{
        runCatching {
            emit(CommonResponse.Loading)
            repo.getRunningList(runningTag = runningTag.tag, request = request)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}