package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.model.type.RunningTag
import com.applemango.runnerbe.presentation.mapper.PostingMapper
import com.applemango.runnerbe.presentation.model.PostingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.applemango.runnerbe.usecaseImpl.bookmark.GetBookmarkedPostsUseCase
import com.applemango.runnerbe.usecaseImpl.bookmark.UpdateBookmarkUseCase
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val getBookmarkedPostsUseCase: GetBookmarkedPostsUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val postingMapper: PostingMapper
): ViewModel() {

    private val selectedTag : MutableStateFlow<RunningTag> = MutableStateFlow(RunningTag.All)
    private val bookmarkList : MutableStateFlow<List<PostingModel>> = MutableStateFlow(emptyList())
    val bookmarkListSize : MutableStateFlow<Int> = MutableStateFlow(0)

    val filteredBookmark: Flow<List<PostingModel>> = combine(
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

    fun addOrRemoveBookmarkedPost(post: PostingModel) {
        val postList: MutableList<PostingModel> = bookmarkList.value.toMutableList()
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
            getBookmarkedPostsUseCase().collect {
                bookmarkList.value = it.map { posting ->
                    postingMapper.mapToPresentation(posting)
                }
            }
        }
    }

    fun bookmarkStatusChange(post: PostingModel) {
        viewModelScope.launch {
            val result = updateBookmarkUseCase(
                post.postId,
                if (!post.bookmarkCheck()) "Y" else "N"
            )
            if (result.isSuccess) {
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
    }
}