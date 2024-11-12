package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.GetPostDetailResponse
import com.applemango.runnerbe.domain.usecase.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.domain.usecase.post.GetPostDetailUseCase
import com.applemango.runnerbe.domain.usecase.post.PostDetailManufacture
import com.applemango.runnerbe.presentation.model.listener.AttendanceAccessionClickListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostAttendanceAccessionViewModel @Inject constructor(
    private val attendanceAccessionUseCase: AttendanceAccessionUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase
) : ViewModel() {
    val userListFlow: MutableStateFlow<List<UserInfo>> = MutableStateFlow(emptyList())

    var postId: Int? = null
    val isSubmit: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _submitState: MutableSharedFlow<UiState> = MutableSharedFlow()
    val submitState: SharedFlow<UiState> get() = _submitState

    fun getUserList(
        postId: Int,
        userId: Int
    ) {
        this.postId = postId
        viewModelScope.launch {
            getPostDetailUseCase(postId, userId).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        val response = it.body as PostDetailManufacture
                        userListFlow.value = response.runnerInfo ?: emptyList()
                    }

                    else -> {
                        userListFlow.value = emptyList()
                    }
                }
            }
        }
    }

    fun attendanceAccession() = viewModelScope.launch {
        postId?.let { postId ->
            attendanceAccessionUseCase(
                postId = postId,
                userIds = userListFlow.value.map { it.userId.toString() },
                whetherAttendList = userListFlow.value.map { if (it.attendance == 1) "Y" else "N" }
            ).collect {
                _submitState.emit(
                    when (it) {
                        is CommonResponse.Success<*> -> {
                            if (it.body is BaseResponse) {
                                if (it.body.isSuccess) UiState.Success(it.code)
                                else UiState.Failed(it.body.message ?: "error")
                            } else UiState.Failed("서버에 문제가 발생했습니다.")
                        }

                        is CommonResponse.Failed -> UiState.Failed(it.message)
                        is CommonResponse.Loading -> UiState.Loading
                        else -> UiState.Empty
                    }
                )
            }
        }
    }

    fun select() {
        isSubmit.value = userListFlow.value.any { it.attendanceState != null }
    }
}