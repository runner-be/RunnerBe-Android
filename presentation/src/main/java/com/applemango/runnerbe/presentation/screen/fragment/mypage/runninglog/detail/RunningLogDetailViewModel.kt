package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.presentation.model.RunningLogDetailModel
import com.applemango.runnerbe.usecaseImpl.runninglog.DeleteRunningLogUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.GetRunningLogDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningLogDetailViewModel @Inject constructor(
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase,
    private val deleteRunningLogUseCase: DeleteRunningLogUseCase,
    private val runningLogDetailMapper: RunningLogDetailMapper,
) : ViewModel() {
    private val _deleteRunningLogResultFlow = MutableSharedFlow<Boolean>()
    val deleteRunningLogResultFlow: SharedFlow<Boolean> = _deleteRunningLogResultFlow.asSharedFlow()

    private val _runningLogDetail = MutableStateFlow<RunningLogDetailModel?>(null)
    val runningLogDetail: StateFlow<RunningLogDetailModel?> get() = _runningLogDetail

    fun getLogDetail(
        userId: Int, logId: Int
    ) {
        viewModelScope.launch {
            getRunningLogDetailUseCase(userId, logId)
                .collectLatest { response ->
                    val result = runningLogDetailMapper.mapToPresentation(response)
                    _runningLogDetail.value = result
                }
        }
    }

    fun deleteRunningLog(logId: Int) {
        viewModelScope.launch {
            val result = deleteRunningLogUseCase(logId)
            _deleteRunningLogResultFlow.emit(result.isSuccess)
        }
    }
}