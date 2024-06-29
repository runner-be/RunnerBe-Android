package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.domain.usecase.bookmark.BookMarkStatusChangeUseCase
import com.applemango.runnerbe.presentation.model.listener.PostClickListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val bookMarkStatusChangeUseCase: BookMarkStatusChangeUseCase
) : ViewModel() {
    val action: MutableSharedFlow<MyPostActions> = MutableSharedFlow()

    private val _changedBookMarkPost : MutableSharedFlow<Posting> = MutableSharedFlow()
    val changedBookMarkPost get() = _changedBookMarkPost

    fun writeClickEvent() = viewModelScope.launch {
        action.emit(MyPostActions.MoveToWrite)
    }

    fun getMyPostClickListener() = object : PostClickListener {

        override fun logWriteClick(post: Posting) {}

        override fun attendanceSeeClick(post: Posting) {
            viewModelScope.launch { action.emit(MyPostActions.MoveToMyPostAttendanceSee(post)) }
        }

        override fun attendanceManageClick(post: Posting) {
            viewModelScope.launch { action.emit(MyPostActions.MoveToAttendanceManage(post)) }
        }

        override fun bookMarkClick(post: Posting) {
            bookmarkStatusChange(post)
        }

        override fun postClick(post: Posting) {
        }
    }

    fun bookmarkStatusChange(post: Posting) = viewModelScope.launch {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId > 0) {
            bookMarkStatusChangeUseCase(
                userId,
                post.postId,
                if (!post.bookmarkCheck()) "Y" else "N"
            ).collect {
                when(it) {
                    is CommonResponse.Success<*> -> {
                        if(it.body is BaseResponse && it.body.isSuccess) {
                            post.bookMark = if(post.bookmarkCheck()) 0 else 1
                            _changedBookMarkPost.emit(post)
                        }
                    }
                }
            }
        }
    }
}

sealed class MyPostActions {
    object MoveToWrite : MyPostActions()
    data class MoveToMyPostAttendanceSee(val post: Posting): MyPostActions()
    data class MoveToAttendanceManage(val post: Posting): MyPostActions()
}