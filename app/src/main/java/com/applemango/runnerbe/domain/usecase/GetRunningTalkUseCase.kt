package com.applemango.runnerbe.domain.usecase

import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.domain.repository.RunningTalkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunningTalkUseCase @Inject constructor(private val repo : RunningTalkRepository) {
    operator fun invoke() : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.getRunningTalks()
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }
}