package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.type.GenderTag
import com.applemango.runnerbe.presentation.model.type.JobButtonId
import com.applemango.runnerbe.presentation.model.NewUserModel
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.usecaseImpl.user.RegisterUserUseCase
import com.applemango.runnerbe.usecaseImpl.user.RegisterUserUseCase.JoinUserParam
import com.applemango.runnerbe.usecaseImpl.user.local.GetDeviceTokenUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.GetUuidUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateJwtTokenUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdditionalInfoViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getDeviceTokenUseCase: GetDeviceTokenUseCase,
    private val getUuidUseCase: GetUuidUseCase,
    private val updateUserIdUseCase: UpdateUserIdUseCase,
    private val updateJwtTokenUseCase: UpdateJwtTokenUseCase,
): ViewModel() {

    var yearOfBrith : String? = null
    val genderRadioChecked : MutableStateFlow<Int> = MutableStateFlow(-1)
    val jobRadioChecked : MutableStateFlow<Int> = MutableStateFlow(-1)

    private val _registerState : MutableSharedFlow<UiState> = MutableSharedFlow()
    val registerState : SharedFlow<UiState> get() = _registerState

    private val _actions: MutableSharedFlow<AdditionalInfoAction> = MutableSharedFlow()
    val actions: SharedFlow<AdditionalInfoAction> get() = _actions

    private fun getJobTag(): String? = JobButtonId.findById(jobRadioChecked.value)?.job

    fun setJobTag(jobTag : String) {
        jobRadioChecked.value = JobButtonId.findByJob(jobTag)?.id?:-1
    }

    fun register() = viewModelScope.launch {
        _registerState.emit(UiState.Loading)
        val deferredDeviceToken = async { getDeviceTokenUseCase() }
        val deferredUuid = async { getUuidUseCase() }

        val deviceToken = deferredDeviceToken.await()
        val uuid = deferredUuid.await()

        if(deviceToken != null && uuid != null) {
            yearOfBrith?.let {
                runCatching {
                    val year = it.toInt()
                    getJobTag()?.let { job ->
                        val result: NewUserModel? = try {
                            val registerResult = registerUserUseCase(
                                JoinUserParam(
                                    uuid = uuid,
                                    deviceToken = deviceToken,
                                    birthday = year,
                                    jobTag = job,
                                    genderTag = getGenderTag(genderRadioChecked.value)
                                )
                            )
                            NewUserModel(
                                registerResult.insertedUserId,
                                registerResult.token,
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }

                        if (result != null) {
                            updateUserIdUseCase(result.insertedUserId)
                            updateJwtTokenUseCase(result.token)
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

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalInfoAction.MoveToBack)
        }
    }

    fun cancelClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalInfoAction.ActivityFinish)
        }
    }

    fun nextClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalInfoAction.MoveToNext)
        }
    }

    private fun getGenderTag(tabId : Int) : String = when(tabId) {
            R.id.maleButton-> GenderTag.MALE.tag
            else -> GenderTag.FEMALE.tag
        }
}

sealed class AdditionalInfoAction {
    object MoveToBack : AdditionalInfoAction()
    object ActivityFinish: AdditionalInfoAction()
    object MoveToNext: AdditionalInfoAction()
}