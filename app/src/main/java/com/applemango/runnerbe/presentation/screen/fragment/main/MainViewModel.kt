package com.applemango.runnerbe.presentation.screen.fragment.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.usecaseImpl.bookmark.UpdateBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
) : ViewModel() {

    val currentItem: MutableSharedFlow<Int> = MutableSharedFlow()
    val clickedPost: MutableStateFlow<PostingModel?> = MutableStateFlow(null)

    private val _bookmarkPost : MutableSharedFlow<PostingModel> = MutableSharedFlow()
    val bookmarkPost get() = _bookmarkPost

    private var _isShowInfoDialog: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isShowInfoDialog get() = _isShowInfoDialog

    fun setTab(index: Int) = viewModelScope.launch {
        currentItem.emit(index)
    }

    fun clickPost(posting: PostingModel?) {
        clickedPost.value = posting
    }

    fun bookmarkStatusChange(post: PostingModel) = viewModelScope.launch {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId == -1) {
            _isShowInfoDialog.emit(true)
            return@launch
        }

        try {
            val result = updateBookmarkUseCase(
                userId,
                post.postId,
                if (!post.bookmarkCheck()) "Y" else "N"
            )
            if (result.isSuccess) {
                _bookmarkPost.emit(post)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}