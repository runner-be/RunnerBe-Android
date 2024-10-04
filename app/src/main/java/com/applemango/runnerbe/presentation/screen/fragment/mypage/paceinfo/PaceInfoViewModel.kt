package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.domain.usecase.myinfo.PatchUserPaceUseCase
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaceInfoViewModel @Inject constructor(val patchUserPaceUseCase: PatchUserPaceUseCase, val savedStateHandle: SavedStateHandle): ViewModel() {
    val paceInfoList: MutableStateFlow<List<PaceSelectItem>> = MutableStateFlow(initPaceInfoList())
    val isConfirmButtonEnabled = combine(paceInfoList) { data ->
        data[0].any { it.isSelected }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(1000L),initialValue = false)
    private val _paceInfoUiState: MutableLiveData<UiState> = MutableLiveData(UiState.Empty)
    val paceInfoUiState get() = _paceInfoUiState

    val inputPage : String? = savedStateHandle["mode"]

    private val _action: MutableSharedFlow<PaceInfoRegistAction> = MutableSharedFlow()
    val action :SharedFlow<PaceInfoRegistAction> get() = _action
    fun getPaceInfoSelectListener() : PaceSelectListener = object : PaceSelectListener {
        override fun itemClick(paceSelectItem: PaceSelectItem) {
            val list = ArrayList<PaceSelectItem>().apply {
                addAll(paceInfoList.value.map { item ->
                    val copy = item.copy()
                    copy.apply {
                        isSelected = copy.pace == paceSelectItem.pace
                    }
                })
            }
            paceInfoList.value = list
        }
    }

    fun backClicked() = viewModelScope.launch {
       _action.emit(PaceInfoRegistAction.MoveToBack)
    }
    fun confirmClicked() = viewModelScope.launch {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        val selectedPace = paceInfoList.value.firstOrNull { it.isSelected } ?: return@launch
        patchUserPaceUseCase(userId, selectedPace.pace).collect {
            if(it is CommonResponse.Success<*>) {
                RunnerBeApplication.mTokenPreference.setMyRunningPace(selectedPace.pace.key)
                _action.emit(PaceInfoRegistAction.ShowCompleteDialog(selectedPace.pace, R.string.pace_complete_title, R.string.pace_complete_description))
            }
            _paceInfoUiState.postValue(
                when(it) {
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
}

sealed class PaceInfoRegistAction {
    object MoveToBack : PaceInfoRegistAction()
    data class ShowCompleteDialog(val pace: Pace, val titleResource: Int, val descriptionResource: Int): PaceInfoRegistAction()
}

enum class PaceRegistrationInputPage(val mode: String) {
    MAP("map"),
    MY_PAGE("myPage");
}