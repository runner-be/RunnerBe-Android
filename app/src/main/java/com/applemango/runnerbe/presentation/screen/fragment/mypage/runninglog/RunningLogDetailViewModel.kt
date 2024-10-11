package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import com.applemango.runnerbe.domain.usecase.runninglog.DeleteRunningLogUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.GetRunningLogDetailUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningLogDetailViewModel @Inject constructor(
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase,
    private val deleteRunningLogUseCase: DeleteRunningLogUseCase
) : ViewModel() {
    val runningLogArgsFlow = MutableStateFlow(Pair(0, 0))
    val runningLogDate = MutableStateFlow("")
    val runningLogGatheringId = MutableStateFlow(0)

    private val _deleteRunningLogResultFlow = MutableSharedFlow<CommonResponse>()
    val deleteRunningLogResultFlow: SharedFlow<CommonResponse> = _deleteRunningLogResultFlow.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val runningLogDetailFlow: Flow<RunningLogDetail> = runningLogArgsFlow
        .filterNotNull()
        .flatMapLatest { logData ->
            getRunningLogDetailUseCase(logData.first, logData.second)
                .map { response ->
                    when (response) {
                        is CommonResponse.Success<*> -> {
                            val runningLogDetail = response.body as? DetailRunningLogResponse
                            runningLogDetail?.let {
                                Log.e("runningLogDetailFlow", it.toString())
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

    fun deleteRunningLog(userId: Int, logId: Int) {
        viewModelScope.launch {
            deleteRunningLogUseCase(userId, logId).collect {
                _deleteRunningLogResultFlow.emit(it)
            }
        }
    }

    fun updateRunningLogGatheringId(gatheringId: Int) {
        runningLogGatheringId.value = gatheringId
    }

    fun updateRunningLogDate(dateString: String) {
        runningLogDate.value = dateString
    }

    fun updateRunningLogArgs(args: Pair<Int, Int>) {
        runningLogArgsFlow.value = args
    }
}