package com.applemango.presentation.ui.screen.fragment.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.bookmark.UpdateBookmarkUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.presentation.ui.model.PostingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {
    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    val currentItem: MutableSharedFlow<Int> = MutableSharedFlow()
    val clickedPost: MutableStateFlow<PostingModel?> = MutableStateFlow(null)

    private val _bookmarkPost : MutableSharedFlow<PostingModel> = MutableSharedFlow()
    val bookmarkPost get() = _bookmarkPost

    private var _isShowInfoDialog: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isShowInfoDialog get() = _isShowInfoDialog

    fun fetchUserId() {
        viewModelScope.launch {
            val id = getUserIdUseCase()
            _userId.value = id
        }
    }

    fun setTab(index: Int) = viewModelScope.launch {
        currentItem.emit(index)
    }

    fun clickPost(posting: PostingModel?) {
        clickedPost.value = posting
    }

    fun bookmarkStatusChange(post: PostingModel) = viewModelScope.launch {
        val userId = getUserIdUseCase()
        if (userId == -1) {
            _isShowInfoDialog.emit(true)
            return@launch
        }

        try {
            val result = updateBookmarkUseCase(
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