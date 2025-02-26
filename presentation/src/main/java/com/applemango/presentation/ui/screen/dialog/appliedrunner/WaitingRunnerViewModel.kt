package com.applemango.presentation.ui.screen.dialog.appliedrunner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.post.AcceptOrDenyRunnerUseCase
import com.applemango.domain.usecaseImpl.post.ClosePostUseCase
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.ui.state.UiState
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
    var post: PostingModel? = null
    var roomId : Int? = null
    private val _waitingInfoList: MutableStateFlow<List<UserModel>> = MutableStateFlow(emptyList())
    val waitingInfoList: StateFlow<List<UserModel>> get() = _waitingInfoList.asStateFlow()

    private val _acceptUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val acceptUiState get() = _acceptUiState

    fun acceptClick(userInfo: UserModel, whetherAccept: String) {
        val postId = post?.postId
        if (postId != null) {
            postWhetherAccept(postId, userInfo, whetherAccept)
        } else _acceptUiState.value = UiState.AnonymousFailed()
    }

    fun updateWaitingInfoList(list: List<UserModel>) {
        _waitingInfoList.value = list
    }

    fun postClose() = viewModelScope.launch {
        val postId = post?.postId
        if (postId != null) {
            val result = closePostUseCase(postId)
            if (result.isSuccess) {
                _acceptUiState.emit(UiState.Success(result.code))
            } else {
                UiState.Failed(result.message ?: "")
            }
        }
    }

    private fun postWhetherAccept(postId: Int, userInfo: UserModel, whetherAccept: String) =
        viewModelScope.launch {
            val result = acceptOrDenyRunnerUseCase(postId, userInfo.userId, whetherAccept)
            if (result.isSuccess) {
                _acceptUiState.emit(UiState.Success(result.code))
            } else {
                _acceptUiState.emit(UiState.Failed(result.message ?: ""))
            }
        }
}