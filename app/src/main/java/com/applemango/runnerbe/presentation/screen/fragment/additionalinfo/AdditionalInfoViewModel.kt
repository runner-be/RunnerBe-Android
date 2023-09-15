package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.JoinUserRequest
import com.applemango.runnerbe.data.network.response.JoinUserResponse
import com.applemango.runnerbe.domain.usecase.RegisterUserUseCase
import com.applemango.runnerbe.presentation.model.GenderTag
import com.applemango.runnerbe.presentation.model.JobButtonId
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.TokenSPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdditionalInfoViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel() {

    var yearOfBrith : String? = null
    val genderRadioChecked : MutableStateFlow<Int> = MutableStateFlow(-1)
    val jobRadioChecked : MutableStateFlow<Int> = MutableStateFlow(-1)

    private val _registerState : MutableSharedFlow<UiState> = MutableSharedFlow()
    val registerState : SharedFlow<UiState> get() = _registerState

    private fun getJobTag(): String? = JobButtonId.findById(jobRadioChecked.value)?.job

    fun setJobTag(jobTag : String) {
        jobRadioChecked.value = JobButtonId.findByJob(jobTag)?.id?:-1
    }

    fun register() = viewModelScope.launch {
        _registerState.emit(UiState.Loading)
        val deviceToken = RunnerBeApplication.mTokenPreference.getDeviceToken() // 디바이스 토큰
        val uuid = RunnerBeApplication.mTokenPreference.getUuid()
        if(deviceToken != null && uuid != null) {
            yearOfBrith?.let {
                runCatching {
                    val year = it.toInt()
                    getJobTag()?.let { job ->
                        registerUserUseCase(
                            JoinUserRequest(
                                uuid = uuid,
                                deviceToken = deviceToken,
                                birthday = year,
                                jobTag = job,
                                genderTag = getGenderTag(genderRadioChecked.value)
                            )
                        ).collect {
                            when(it) {
                                is CommonResponse.Success<*> -> {
                                    if(it.body is JoinUserResponse) {
                                        if(it.body.isSuccess) {
                                            RunnerBeApplication.mTokenPreference.apply {
                                                setUserId(it.body.result.insertedUserId)
                                                setToken(it.body.result.token)
                                            }
                                            _registerState.emit(UiState.Success(it.code))
                                        } else _registerState.emit(UiState.Failed(it.body.message?:""))
                                    }
                                }
                                is CommonResponse.Failed -> {
                                    _registerState.emit(UiState.Failed(it.message))
                                }
                            }
                        }
                    }?:run {
                        _registerState.emit(UiState.Failed("직업을 선택해주세요."))
                    }
                }.onFailure {
                    _registerState.emit(UiState.Failed(it.message?:"출생년도를 다시 입력해주세요."))
                }
            }?:run {
                _registerState.emit(UiState.Failed("출생년도를 다시 입력해주세요."))
            }
        } else _registerState.emit(UiState.Failed("앱을 재실행해주세요."))

    }

    fun getGenderTag(tabId : Int) : String = when(tabId) {
            R.id.maleButton-> GenderTag.MALE.tag
            else -> GenderTag.FEMALE.tag
        }
}