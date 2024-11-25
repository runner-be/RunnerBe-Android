package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.GetMyPageResult
import com.applemango.runnerbe.data.network.response.UserDataResponse
import com.applemango.runnerbe.domain.usecase.GetUserDataUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    val selectedCategoryId: MutableStateFlow<JoinedRunningCategory> = MutableStateFlow(
        JoinedRunningCategory.ALL
    )
    private val userPostings: MutableStateFlow<List<Posting>> = MutableStateFlow(emptyList())

    val userPostingSize: MutableStateFlow<Int> = MutableStateFlow(0)

    val userPostingFlow: Flow<List<Posting>> = combine(
        userPostings,
        selectedCategoryId
    ) { postings, categoryId ->
        when (categoryId) {
            JoinedRunningCategory.ALL -> postings
            JoinedRunningCategory.MY -> {
                val userId = RunnerBeApplication.mTokenPreference.getUserId()
                postings.filter {
                    it.postUserId == userId
                }
            }
        }.sortedBy {
            it.gatheringTime
        }.also {
            userPostingSize.value = it.size
        }
    }

    fun updatePostBookmark(post: Posting) {
        val postList: MutableList<Posting> = userPostings.value.toMutableList()
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

    fun getUserRunningPostings(userId: Int) {
        viewModelScope.launch {
            getUserDataUseCase(userId)
                .collectLatest { response ->
                    when (response) {
                        is CommonResponse.Success<*> -> {
                            if (response.body is UserDataResponse) {
                                val result = response.body.result as? GetMyPageResult
                                userPostings.value = result?.myRunning ?: emptyList()
                            }
                        }

                        else -> {
                            throw Exception("")
                        }
                    }
                }
        }
    }

    fun updateSelectedCategory(category: JoinedRunningCategory) {
        selectedCategoryId.value = category
    }
}