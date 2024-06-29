package com.applemango.runnerbe.presentation.screen.fragment.main

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookMarkStatusChangeUseCase: BookMarkStatusChangeUseCase
) : ViewModel() {

    val currentItem: MutableSharedFlow<Int> = MutableSharedFlow()
    var lastTabIndex : Int = 0
    val clickedPost: MutableStateFlow<Posting?> = MutableStateFlow(null)

    private val _bookmarkPost : MutableSharedFlow<Posting> = MutableSharedFlow()
    val bookmarkPost get() = _bookmarkPost

    private var _isShowInfoDialog: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isShowInfoDialog get() = _isShowInfoDialog

    fun setTab(index: Int) = viewModelScope.launch {
        currentItem.emit(index)
    }

    fun clickPost(posting: Posting?) {
        clickedPost.value = posting
    }

    fun getPostClickListener() = object: PostClickListener {
        override fun logWriteClick(post: Posting) {}

        override fun attendanceSeeClick(post: Posting) {}

        override fun attendanceManageClick(post: Posting) {}

        override fun bookMarkClick(post: Posting) {
            bookmarkStatusChange(post)
        }

        override fun postClick(post: Posting) {
            clickedPost.value = if (clickedPost.value == post) null else post
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
                            _bookmarkPost.emit(post)
                        }
                    }
                }
            }
        } else _isShowInfoDialog.emit(true)
    }
}