package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.GetPostDetailResponse
import com.applemango.runnerbe.domain.usecase.post.GetPostDetailUseCase
import com.applemango.runnerbe.domain.usecase.post.PostDetailManufacture
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostAttendanceSeeViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase
) : ViewModel() {

    val userListFlow: MutableStateFlow<List<UserInfo>> = MutableStateFlow(emptyList())

    fun getUserList(
        postId: Int,
        userId: Int
    ) {
        viewModelScope.launch {
            getPostDetailUseCase(postId, userId).collectLatest {
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
}