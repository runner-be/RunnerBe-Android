package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.UserMapper
import com.applemango.runnerbe.presentation.model.UserModel
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.usecaseImpl.post.GetPostDetailUseCase
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostAttendanceAccessionViewModel @Inject constructor(
    private val attendanceAccessionUseCase: AttendanceAccessionUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val userMapper: UserMapper
) : ViewModel() {
    val userListFlow: MutableStateFlow<List<UserModel>> = MutableStateFlow(emptyList())

    var postId: Int? = null
    val isSubmit: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _submitState: MutableSharedFlow<UiState> = MutableSharedFlow()
    val submitState: SharedFlow<UiState> get() = _submitState

    fun getUserList(
        postId: Int,
    ) {
        this.postId = postId
        viewModelScope.launch {
            getPostDetailUseCase(postId).collect {
                userListFlow.value = it.runnerInfo?.map { user ->
                    userMapper.mapToPresentation(user)
                } ?: emptyList()
            }
        }
    }

    fun attendanceAccession() = viewModelScope.launch {
        postId?.let { postId ->
            val result = attendanceAccessionUseCase(
                postId = postId,
                userIds = userListFlow.value.map { it.userId.toString() },
                whetherAttendList = userListFlow.value.map { if (it.attendance == 1) "Y" else "N" }
            )

            if (result.isSuccess) {
                _submitState.emit(UiState.Success(result.code))
            } else {
                _submitState.emit(UiState.Failed(result.message ?: ""))
            }
        }
    }

    fun select() {
        isSubmit.value = userListFlow.value.any { it.attendanceState != null }
    }
}