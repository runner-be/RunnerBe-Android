package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.domain.usecase.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.presentation.model.listener.AttendanceAccessionClickListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostAttendanceAccessionViewModel @Inject constructor(
    private val attendanceAccessionUseCase: AttendanceAccessionUseCase
) : ViewModel() {

    val userList : ObservableArrayList<UserInfo> = ObservableArrayList()
    var postId : Int? = null
    val isSubmit : MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _submitState : MutableSharedFlow<UiState> = MutableSharedFlow()
    val submitState :SharedFlow<UiState> get() = _submitState

    fun userListUpdate(users : List<UserInfo>) {
        userList.clear()
        userList.addAll(users)
        select()
    }

    fun getAccessionClickListener() = object : AttendanceAccessionClickListener {
        override fun onAcceptClick(userInfo: UserInfo) {
            select()
            userInfo.attendance = 1
        }

        override fun onRefuseClick(userInfo: UserInfo) {
            select()
            userInfo.attendance = 0
        }
    }

    fun attendanceAccession() =viewModelScope.launch {
        postId?.let { postId ->
            attendanceAccessionUseCase(
                postId = postId,
                userIds = userList.map { it.userId.toString() },
                whetherAttendList = userList.map { if(it.attendance == 1) "Y" else "N" }
            ).collect {
                _submitState.emit(
                    when(it) {
                        is CommonResponse.Success<*> -> {
                            if(it.body is BaseResponse) {
                                if(it.body.isSuccess) UiState.Success(it.code)
                                else UiState.Failed(it.body.message?:"error")
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
        isSubmit.value = userList.any { it.attendanceState != null }
    }
}