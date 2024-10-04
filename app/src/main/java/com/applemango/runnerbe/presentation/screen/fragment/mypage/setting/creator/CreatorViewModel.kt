package com.applemango.runnerbe.presentation.screen.fragment.mypage.setting.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.model.CreatorImageAndPosition
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CreatorViewModel : ViewModel() {
    val creatorList : List<CreatorImageAndPosition> = CreatorImageAndPosition.values().toList()

    private val _actions: MutableSharedFlow<CreatorAction> = MutableSharedFlow()
    val actions: SharedFlow<CreatorAction> get() = _actions

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(CreatorAction.MoveToBack)
        }
    }
}

sealed class CreatorAction {
    object MoveToBack: CreatorAction()
}