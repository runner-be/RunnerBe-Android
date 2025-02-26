package com.applemango.presentation.ui.screen.fragment.mypage.mypost.see

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.post.GetPostDetailUseCase
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostAttendanceSeeViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val userMapper: UserMapper
) : ViewModel() {

    val userListFlow: MutableStateFlow<List<UserModel>> = MutableStateFlow(emptyList())

    fun getUserList(
        postId: Int,
    ) {
        viewModelScope.launch {
            getPostDetailUseCase(postId).collectLatest {
                userListFlow.value = it.runnerInfo?.map { user ->
                    userMapper.mapToPresentation(user)
                } ?: emptyList()
            }
        }
    }
}