package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.year

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.util.getNowYear
import com.applemango.runnerbe.util.getYearListByYear
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AdditionalYearInfoSelectViewModel: ViewModel() {

    val yearList = getYearListByYear(19, 80)
    val isUnder19 : MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _actions: MutableSharedFlow<AdditionalYearInfoSelectAction> = MutableSharedFlow()
    val actions: SharedFlow<AdditionalYearInfoSelectAction> get() = _actions

    fun selectYear(year: String) {
        isUnder19.value = try {
            getNowYear().toInt() - year.toInt() <= 19
        }catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalYearInfoSelectAction.MoveToBack)
        }
    }

    fun cancelClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalYearInfoSelectAction.ActivityFinish)
        }
    }

    fun nextClicked() {
        viewModelScope.launch {
            _actions.emit(AdditionalYearInfoSelectAction.MoveToNext)
        }
    }
}

sealed class AdditionalYearInfoSelectAction {
    object MoveToBack: AdditionalYearInfoSelectAction()
    object ActivityFinish: AdditionalYearInfoSelectAction()
    object MoveToNext: AdditionalYearInfoSelectAction()
}