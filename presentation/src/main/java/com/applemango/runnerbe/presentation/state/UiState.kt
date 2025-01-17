package com.applemango.runnerbe.presentation.state

sealed class UiState {
    data class Success(val code :Int) : UiState()
    data class Failed(val message : String) : UiState()

    data class AnonymousFailed(val message: String? = null) : UiState()
    object NetworkError : UiState()
    object Loading : UiState()
    object Empty : UiState()
}