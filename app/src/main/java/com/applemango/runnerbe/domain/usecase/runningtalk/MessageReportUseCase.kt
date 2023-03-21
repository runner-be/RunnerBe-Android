package com.applemango.runnerbe.domain.usecase.runningtalk

import com.applemango.runnerbe.domain.repository.RunningTalkRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageReportUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    operator fun invoke(messageIdList : List<String>) : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.reportMessage(messageIdList)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message?:"error"))
        }
    }
}