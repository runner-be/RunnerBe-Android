package com.applemango.runnerbe.presentation.screen.fragment.mypage.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.BuildConfig
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.usecaseImpl.alarm.UpdateAlarmUseCase
import com.applemango.runnerbe.usecaseImpl.user.WithdrawalUserUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val withdrawalUserUseCase: WithdrawalUserUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase
) : ViewModel() {

    private val _logoutState: MutableLiveData<Boolean> = MutableLiveData()
    val logoutState: LiveData<Boolean> = _logoutState
    private val _withdrawalState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val withdrawalState: StateFlow<UiState> get() = _withdrawalState

    var beforeAlarmCheck : Boolean = false

    fun logout() = viewModelScope.launch {
        runCatching {
            RunnerBeApplication.mTokenPreference.logoutSet()
        }.onSuccess {
            _logoutState.value = true
        }.onFailure { e ->
            e.printStackTrace()
            _logoutState.value = false
        }
    }

    fun withdrawalUser() = viewModelScope.launch {
        runCatching {
            val result = withdrawalUserUseCase(
                RunnerBeApplication.mTokenPreference.getUserId(),
                BuildConfig.WITHDRAWAL_API_KEY
            )
            if (result.isSuccess) {
                _withdrawalState.emit(UiState.Success(result.code))
            } else {
                _withdrawalState.emit(UiState.Failed(result.message ?: ""))
            }
        }.onFailure {
            _withdrawalState.emit(UiState.NetworkError)
        }
    }

    suspend fun patchAlarm(userId : Int, pushOn : Boolean) {
        runCatching {
            if(beforeAlarmCheck != pushOn) updateAlarmUseCase(userId, pushOn)
        }.onFailure { it.printStackTrace() }
    }
}