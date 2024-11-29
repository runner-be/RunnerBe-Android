package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import com.applemango.runnerbe.domain.usecase.runninglog.DeleteRunningLogUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.GetRunningLogDetailUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningLogDetailViewModel @Inject constructor(
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase,
    private val deleteRunningLogUseCase: DeleteRunningLogUseCase
) : ViewModel() {
    private val _deleteRunningLogResultFlow = MutableSharedFlow<CommonResponse>()
    val deleteRunningLogResultFlow: SharedFlow<CommonResponse> = _deleteRunningLogResultFlow.asSharedFlow()

    private val _runningLogDetail = MutableStateFlow<RunningLogDetail?>(null)
    val runningLogDetail: StateFlow<RunningLogDetail?> get() = _runningLogDetail.asStateFlow()

    fun getLogDetail(
        userId: Int, logId: Int
    ) {
        viewModelScope.launch {
            getRunningLogDetailUseCase(userId, logId)
                .collectLatest { response ->
                    when (response) {
                        is CommonResponse.Success<*> -> {
                            val runningLogDetail = response.body as? DetailRunningLogResponse
                            _runningLogDetail.value = runningLogDetail?.let {
                                parseRunningLogDetailToPresentation(it)
                            }
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
    }

    fun deleteRunningLog(userId: Int, logId: Int) {
        viewModelScope.launch {
            deleteRunningLogUseCase(userId, logId).collect {
                _deleteRunningLogResultFlow.emit(it)
            }
        }
    }
}