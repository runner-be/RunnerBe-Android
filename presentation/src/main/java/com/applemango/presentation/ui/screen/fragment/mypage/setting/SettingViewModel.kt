package com.applemango.presentation.ui.screen.fragment.mypage.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.alarm.UpdateAlarmUseCase
import com.applemango.domain.usecaseImpl.user.WithdrawalUserUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.domain.usecaseImpl.user.local.LogoutUseCase
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val withdrawalUserUseCase: WithdrawalUserUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private val _logoutState: MutableLiveData<Boolean> = MutableLiveData()
    val logoutState: LiveData<Boolean> = _logoutState
    private val _withdrawalState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val withdrawalState: StateFlow<UiState> get() = _withdrawalState

    var beforeAlarmCheck : Boolean = false

    fun fetchUserId() {
        viewModelScope.launch {
            _userId.value = getUserIdUseCase()
        }
    }

    fun logout() = viewModelScope.launch {
        runCatching {
            logoutUseCase()
        }.onSuccess {
            _logoutState.value = true
        }.onFailure { e ->
            e.printStackTrace()
            _logoutState.value = false
        }
    }

    fun withdrawalUser() {
        viewModelScope.launch {
            val result = withdrawalUserUseCase(
                BuildConfig.WITHDRAWAL_API_KEY
            )
            if (result.isSuccess) {
                _withdrawalState.emit(UiState.Success(result.code))
            } else {
                _withdrawalState.emit(UiState.Failed(result.message ?: ""))
            }
        }
    }

    suspend fun patchAlarm(userId : Int, pushOn : Boolean) {
        runCatching {
            if(beforeAlarmCheck != pushOn) updateAlarmUseCase(pushOn)
        }.onFailure { it.printStackTrace() }
    }
}