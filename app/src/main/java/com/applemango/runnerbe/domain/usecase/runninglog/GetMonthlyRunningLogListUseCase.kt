package com.applemango.runnerbe.domain.usecase.runninglog

import android.util.Log
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMonthlyRunningLogListUseCase @Inject constructor(
    private val runningLogRepository: RunningLogRepository
) {
    operator fun invoke(userId: Int, year: Int, month: Int): Flow<CommonResponse> = flow {
        kotlin.runCatching {
            runningLogRepository.getMonthlyRunningLogList(userId, year, month)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message?:"error"))
        }
    }
}