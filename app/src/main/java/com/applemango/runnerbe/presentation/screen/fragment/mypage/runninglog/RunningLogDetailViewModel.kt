package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.network.response.DetailRunningLog
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import com.applemango.runnerbe.domain.usecase.runninglog.GetRunningLogDetailUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningLogDetailViewModel @Inject constructor(
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase
) : ViewModel() {
    val runningLogArgs = MutableStateFlow<Pair<Int, Int>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val runningLogDetailFlow: Flow<RunningLogDetail> = runningLogArgs
        .filterNotNull()
        .flatMapLatest { logData ->
            getRunningLogDetailUseCase(logData.first, logData.second)
                .map { response ->
                    when (response) {
                        is CommonResponse.Success<*> -> {
                            val runningLogDetail = response.body as? DetailRunningLogResponse
                            runningLogDetail?.let {
                                parseRunningLogDetailToPresentation(it)
                            } ?: throw IllegalStateException("Invalid running log data")
                        }

                        is CommonResponse.Failed -> {
                            throw Exception(response.message)
                        }

                        else -> {
                            throw Exception("RunningLogDetailViewModel-runningLogDetailFlow-when-else")
                        }
                    }
                }
        }

    fun updateRunningLogArgs(args: Pair<Int, Int>) {
        runningLogArgs.value = args
    }
}