package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.domain.usecase.post.ClosePostUseCase
import com.applemango.runnerbe.domain.usecase.post.AcceptOrDenyRunnerUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingRunnerViewModel @Inject constructor(
    private val acceptOrDenyRunnerUseCase: AcceptOrDenyRunnerUseCase,
    private val closePostUseCase: ClosePostUseCase
) : ViewModel() {
    var post: Posting? = null
    var roomId : Int? = null
    private val _waitingInfoList: MutableStateFlow<List<UserInfo>> = MutableStateFlow(emptyList())
    val waitingInfoList: StateFlow<List<UserInfo>> get() = _waitingInfoList.asStateFlow()

    private val _acceptUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val acceptUiState get() = _acceptUiState

    fun acceptClick(userInfo: UserInfo, whetherAccept: String) {
        val postId = post?.postId
        if (postId != null) {
            postWhetherAccept(postId, userInfo, whetherAccept)
        } else _acceptUiState.value = UiState.AnonymousFailed()
    }

    fun updateWaitingInfoList(list: List<UserInfo>) {
        _waitingInfoList.value = list
    }

    fun postClose() = viewModelScope.launch {
        val postId = post?.postId
        if (postId != null) {
            closePostUseCase(postId).collect {
                _acceptUiState.emit(
                    when (it) {
                        is CommonResponse.Success<*> -> {
                            UiState.Success(it.code)
                        }
                        is CommonResponse.Failed -> {
                            if (it.code >= 999) UiState.NetworkError
                            else UiState.Failed(it.message)
                        }
                        is CommonResponse.Loading -> UiState.Loading
                        else -> UiState.Empty
                    }
                )
            }
        }
    }

    private fun postWhetherAccept(postId: Int, userInfo: UserInfo, whetherAccept: String) =
        viewModelScope.launch {
            acceptOrDenyRunnerUseCase(postId, userInfo.userId, whetherAccept).collect {
                _acceptUiState.emit(
                    when (it) {
                        is CommonResponse.Success<*> -> {
                            val itemRemovedList = waitingInfoList.value.toMutableList()
                            itemRemovedList.remove(userInfo)
                            _waitingInfoList.value = itemRemovedList
                            UiState.Success(it.code)
                        }
                        is CommonResponse.Failed -> {
                            if (it.code >= 999) UiState.NetworkError
                            else if (it.code == 700) UiState.AnonymousFailed()
                            else UiState.Failed(it.message)
                        }
                        is CommonResponse.Loading -> UiState.Loading
                        else -> UiState.Empty
                    }
                )
            }
        }
}