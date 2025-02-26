package com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.user.GetUserDataUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.presentation.ui.mapper.PostingMapper
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.JoinedRunningCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 추후 마이페이지 API 변경 시 동일하게 적용할 것
 * @author 로키
 */
@HiltViewModel
class JoinedRunningViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper,
) : ViewModel() {
    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    val selectedCategoryId: MutableStateFlow<JoinedRunningCategory> = MutableStateFlow(
        JoinedRunningCategory.ALL
    )
    private val userPostings: MutableStateFlow<List<PostingModel>> = MutableStateFlow(emptyList())

    val userPostingSize: MutableStateFlow<Int> = MutableStateFlow(0)

    val userPostingFlow: Flow<List<PostingModel>> = combine(
        userPostings,
        selectedCategoryId
    ) { postings, categoryId ->
        when (categoryId) {
            JoinedRunningCategory.ALL -> postings
            JoinedRunningCategory.MY -> {
                val userId = getUserIdUseCase()
                postings.filter {
                    it.postUserId == userId
                }
            }
        }.sortedByDescending {
            it.gatheringTime
        }.also {
            userPostingSize.value = it.size
        }
    }

    fun updatePostBookmark(post: PostingModel) {
        val postList: MutableList<PostingModel> = userPostings.value.toMutableList()
        val parsedPostList = postList.map { item ->
            if (item.postId == post.postId) {
                val prevBookmark = if (post.bookMark == 1) 0 else 1
                item.copy(bookMark = prevBookmark)
            } else {
                item
            }
        }
        this.userPostings.value = parsedPostList
    }

    fun getUserRunningPostings() {
        viewModelScope.launch {
            getUserDataUseCase()
                .collectLatest { response ->
                    val result = response.myRunning.map {
                        postingMapper.mapToPresentation(it)
                    }
                    userPostings.value = result
                }
        }
    }

    fun updateSelectedCategory(category: JoinedRunningCategory) {
        selectedCategoryId.value = category
    }
}