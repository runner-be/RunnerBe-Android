package com.applemango.runnerbe.presentation.screen.fragment.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WebViewViewModel: ViewModel() {

    val title: MutableStateFlow<String> = MutableStateFlow("")
    private val _actions: MutableSharedFlow<WebViewAction> = MutableSharedFlow()
    val actions: SharedFlow<WebViewAction> get() = _actions

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(WebViewAction.MoveToBack)
        }
    }
}

sealed class WebViewAction {
    object MoveToBack: WebViewAction()
}