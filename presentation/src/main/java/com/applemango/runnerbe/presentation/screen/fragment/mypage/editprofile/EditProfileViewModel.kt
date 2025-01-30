package com.applemango.runnerbe.presentation.screen.fragment.mypage.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.presentation.model.UserModel
import com.applemango.runnerbe.usecaseImpl.user.UpdateJobUseCase
import com.applemango.runnerbe.usecaseImpl.user.UpdateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val updateJobUseCase: UpdateJobUseCase,
): ViewModel() {

    val userInfo : MutableLiveData<UserModel> = MutableLiveData()
    val radioChecked : MutableLiveData<Int> = MutableLiveData()
    val name: MutableStateFlow<String> = MutableStateFlow("")
    var currentJob : String = ""
    private val _nicknameChangeState : MutableLiveData<UiState> = MutableLiveData()
    val nicknameChangeState get() = _nicknameChangeState

    private val _jobChangeState : MutableLiveData<UiState> = MutableLiveData()
    val jobChangeState get() = _jobChangeState

    fun nicknameChange(changedNickname : String) = viewModelScope.launch {
        val result = updateNicknameUseCase(changedNickname)
        if (result.isSuccess) {
            _nicknameChangeState.postValue(UiState.Success(result.code))
        } else {
            _nicknameChangeState.postValue(UiState.Failed(result.message ?: ""))
        }
    }

    fun jobChange(changedJob: String) = viewModelScope.launch {
        val result = updateJobUseCase(changedJob)
        if (result.isSuccess) {
            _jobChangeState.postValue(UiState.Success(result.code))
        } else {
            _jobChangeState.postValue(UiState.Failed(result.message ?: ""))
        }
    }

    fun isNameChangeEnable(name: String, nameChange: String) = nameChange == "N" && name.isNotEmpty()
}