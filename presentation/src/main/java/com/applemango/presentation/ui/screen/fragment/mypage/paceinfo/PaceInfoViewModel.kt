package com.applemango.presentation.ui.screen.fragment.mypage.paceinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.user.UpdateUserPaceUseCase
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.ui.model.type.Pace
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaceInfoViewModel @Inject constructor(
    val updateUserPaceUseCase: UpdateUserPaceUseCase,
    paceInfoProvider: PaceInfoProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val paceInfoList: MutableStateFlow<List<PaceSelectItem>> = MutableStateFlow(paceInfoProvider.initPaceInfoList())
    val isConfirmButtonEnabled = combine(paceInfoList) { data ->
        data[0].any { it.isSelected }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = false
    )
    private val _paceInfoUiState: MutableLiveData<UiState> = MutableLiveData(UiState.Empty)
    val paceInfoUiState get() = _paceInfoUiState

    val inputPage: String? = savedStateHandle["mode"]

    private val _action: MutableSharedFlow<PaceInfoRegistAction> = MutableSharedFlow()
    val action: SharedFlow<PaceInfoRegistAction> get() = _action
    fun getPaceInfoSelectListener(): PaceSelectListener = object : PaceSelectListener {
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

    fun patchUserPace() {
        viewModelScope.launch {
            val selectedPace = paceInfoList.value.firstOrNull { it.isSelected } ?: return@launch
            val result = updateUserPaceUseCase(selectedPace.pace.key)
            if (result.isSuccess) {
                _action.emit(
                    PaceInfoRegistAction.ShowCompleteDialog(
                        selectedPace.pace,
                        R.string.pace_complete_title,
                        R.string.pace_complete_description
                    )
                )
            } else {
                UiState.Failed(result.message ?: "")
            }
        }
    }

}

sealed class PaceInfoRegistAction {
    object MoveToBack : PaceInfoRegistAction()
    data class ShowCompleteDialog(
        val pace: Pace,
        val titleResource: Int,
        val descriptionResource: Int
    ) : PaceInfoRegistAction()
}

enum class PaceRegistrationInputPage(val mode: String) {
    MAP("map"),
    MY_PAGE("myPage");
}