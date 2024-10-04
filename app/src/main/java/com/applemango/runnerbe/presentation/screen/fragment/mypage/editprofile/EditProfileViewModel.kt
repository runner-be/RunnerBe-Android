package com.applemango.runnerbe.presentation.screen.fragment.mypage.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.domain.usecase.JobChangeUseCase
import com.applemango.runnerbe.domain.usecase.myinfo.NicknameChangeUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val nicknameChangeUseCase: NicknameChangeUseCase,
    private val jobChangeUseCase: JobChangeUseCase
): ViewModel() {

    val userInfo : MutableLiveData<UserInfo> = MutableLiveData()
    val radioChecked : MutableLiveData<Int> = MutableLiveData()
    val name: MutableStateFlow<String> = MutableStateFlow("")
    var currentJob : String = ""
    private val _nicknameChangeState : MutableLiveData<UiState> = MutableLiveData()
    val nicknameChangeState get() = _nicknameChangeState

    private val _jobChangeState : MutableLiveData<UiState> = MutableLiveData()
    val jobChangeState get() = _jobChangeState

    fun nicknameChange(changedNickname : String) = viewModelScope.launch {
        nicknameChangeUseCase(RunnerBeApplication.mTokenPreference.getUserId(), changedNickname).collect {
            _nicknameChangeState.postValue(
                when(it) {
                is CommonResponse.Success<*> -> UiState.Success(it.code)
                is CommonResponse.Failed -> {
                    if (it.code <= 999) UiState.NetworkError
                    else UiState.Failed(it.message)
                }
                is CommonResponse.Loading -> UiState.Loading
                else -> UiState.Empty
            })
        }
    }

    fun jobChange(changedJob: String) = viewModelScope.launch {
        jobChangeUseCase(RunnerBeApplication.mTokenPreference.getUserId(), changedJob).collect {
            _jobChangeState.postValue(
                when(it) {
                    is CommonResponse.Success<*> -> UiState.Success(it.code)
                    is CommonResponse.Failed -> {
                        if (it.code <= 999) UiState.NetworkError
                        else UiState.Failed(it.message)
                    }
                    is CommonResponse.Loading -> UiState.Loading
                    else -> UiState.Empty
                }
            )
        }
    }

    fun isNameChangeEnable(name: String, nameChange: String) = nameChange == "N" && name.isNotEmpty()
}