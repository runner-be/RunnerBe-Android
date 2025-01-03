package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUser
import com.applemango.runnerbe.domain.usecase.user.GetOtherUserDataUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserJoinedPostViewModel @Inject constructor(
    private val getOtherUserDataUseCase: GetOtherUserDataUseCase
) : ViewModel() {
    val targetNickname = MutableStateFlow<String?>(null)
    val postSize = MutableStateFlow<Int>(0)

    private var _postList: MutableStateFlow<List<Posting>> = MutableStateFlow(emptyList())
    val postList: StateFlow<List<Posting>> get() = _postList.asStateFlow()

    fun getJoinedPostList(userId: Int) {
        viewModelScope.launch {
            getOtherUserDataUseCase(userId).collectLatest { response ->
                when (response) {
                    is CommonResponse.Success<*> -> {
                        val result = response.body as? OtherUser
                        _postList.value = result?.userPosting?.sortedByDescending {
                            it.gatheringTime
                        } ?: emptyList()
                        postSize.value = _postList.value.size
                    }

                    else -> {

                    }
                }
            }
        }
    }

    fun updateTargetNickname(nickname: String) {
        targetNickname.value = nickname
    }
}