package com.applemango.runnerbe.domain.usecase.runningtalk

import com.applemango.runnerbe.domain.repository.RunningTalkRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunningTalkMessagesUseCase @Inject constructor(private val repo : RunningTalkRepository) {
    operator fun invoke(roomId: Int) :Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.getRunningTalkDetail(roomId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message?:"error"))
        }
    }
}