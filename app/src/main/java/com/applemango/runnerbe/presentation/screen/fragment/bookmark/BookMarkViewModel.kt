package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.usecase.bookmark.*
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val getBookmarkedPostsUseCase: GetBookmarkedPostsUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
): ViewModel() {

    private val selectedTag : MutableStateFlow<RunningTag> = MutableStateFlow(RunningTag.All)
    private val bookmarkList : MutableStateFlow<List<Posting>> = MutableStateFlow(emptyList())
    val bookmarkListSize : MutableStateFlow<Int> = MutableStateFlow(0)

    val filteredBookmark: Flow<List<Posting>> = combine(
        selectedTag,
        bookmarkList
    ) { tag, bookmarkList ->
        when (tag) {
            RunningTag.All -> bookmarkList
            RunningTag.Before -> bookmarkList
                .filter { it.runningTag == RunningTag.Before.tag }
            RunningTag.After -> bookmarkList
                .filter { it.runningTag == RunningTag.After.tag }
            RunningTag.Holiday -> bookmarkList
                .filter { it.runningTag == RunningTag.Holiday.tag }
        }.sortedByDescending {
            it.gatheringTime
        }.also {
            bookmarkListSize.value = it.size
        }
    }

    fun addOrRemoveBookmarkedPost(post: Posting) {
        val postList: MutableList<Posting> = bookmarkList.value.toMutableList()
        if (post.bookmarkCheck()) {
            postList.removeIf { it.postId == post.postId }
        } else {
            val bookmarkedPost = if (post.bookmarkCheck()) {
                post.copy(bookMark = 0)
            } else post.copy(bookMark = 1)
            postList.add(bookmarkedPost)
        }
        this.bookmarkList.value = postList
    }

    fun updateSelectedTag(tag: RunningTag) {
        selectedTag.value = tag
    }

    fun getBookmarkList() {
        viewModelScope.launch {
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            getBookmarkedPostsUseCase(userId = userId).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        val response = it.body as GetBookmarkResponse
                        bookmarkList.value = response.result.bookMarkList ?: emptyList()
                    }

                    is CommonResponse.Failed -> LogUtil.errorLog("getBookmarkList Failed")

                    else -> LogUtil.errorLog("getBookmarkList else")
                }
            }
        }
    }

    fun bookmarkStatusChange(post: Posting) {
        viewModelScope.launch {
            updateBookmarkUseCase(
                RunnerBeApplication.mTokenPreference.getUserId(),
                post.postId,
                if (!post.bookmarkCheck()) "Y" else "N"
            ).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        if (it.body is BaseResponse && it.body.isSuccess) {
                            val updatedList = bookmarkList.value.map { item ->
                                if (item == post) {
                                    val bookmarkStatus = if (item.bookMark == 1) 0 else 1
                                    item.copy(bookMark = bookmarkStatus)
                                } else {
                                    item
                                }
                            }
                            bookmarkList.value = updatedList
                        }
                    }

                    else -> {
                        Log.e(
                            this.javaClass.name,
                            "bookmarkStatusChange - when - else - CommonResponse"
                        )
                    }
                }
            }
        }
    }
}