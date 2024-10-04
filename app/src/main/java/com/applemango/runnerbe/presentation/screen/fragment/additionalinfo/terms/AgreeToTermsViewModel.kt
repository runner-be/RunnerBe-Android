package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AgreeToTermsViewModel : ViewModel() {

    val serviceTerms : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val privacyTerms : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val locationServiceTerms : MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _actions: MutableSharedFlow<AgreeToTermsAction> = MutableSharedFlow()
    val actions: SharedFlow<AgreeToTermsAction> get() = _actions

    val allCheck  = combine(serviceTerms, privacyTerms, locationServiceTerms) { isServiceCheck, isPrivacyCheck, isLocationServiceCheck ->
        isServiceCheck && isLocationServiceCheck && isPrivacyCheck
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = false
    )

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.ActivityFinish)
        }
    }

    fun cancelClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.ActivityFinish)
        }
    }

    fun serviceUseTermsClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.MoveToWebView(
                title = RunnerBeApplication.instance.resources.getString(R.string.terms_of_service),
                url = "https://raw.githubusercontent.com/runner-be/runner-be.github.io/main/Policy_Service.txt"
            ))
        }
    }

    fun privacyTermsClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.MoveToWebView(
                title = RunnerBeApplication.instance.resources.getString(R.string.privacy_policy),
                url = "https://raw.githubusercontent.com/runner-be/runner-be.github.io/main/Policy_Privacy_Collect.txt"
            ))
        }
    }

    fun locationServiceUseTermsClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.MoveToWebView(
                title = RunnerBeApplication.instance.resources.getString(R.string.use_a_location_service),
                url = "https://raw.githubusercontent.com/runner-be/runner-be.github.io/main/Policy_Location.txt"
            ))
        }
    }

    fun nextClicked() {
        viewModelScope.launch {
            _actions.emit(AgreeToTermsAction.MoveToNext)
        }
    }
}

sealed class AgreeToTermsAction {
    object ActivityFinish: AgreeToTermsAction()
    data class MoveToWebView(val title: String, val url: String): AgreeToTermsAction()
    object MoveToNext: AgreeToTermsAction()
}