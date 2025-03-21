package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.user.GetOtherUserDataUseCase
import com.applemango.presentation.ui.mapper.OtherUserMyPageMapper
import com.applemango.presentation.ui.model.PostingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserJoinedPostViewModel @Inject constructor(
    private val getOtherUserDataUseCase: GetOtherUserDataUseCase,
    private val otherUserMyPageMapper: OtherUserMyPageMapper,
) : ViewModel() {
    val targetNickname = MutableStateFlow<String?>(null)
    val postSize = MutableStateFlow(0)

    private var _postList: MutableStateFlow<List<PostingModel>> = MutableStateFlow(emptyList())
    val postList: StateFlow<List<PostingModel>> get() = _postList.asStateFlow()

    fun getJoinedPostList(userId: Int) {
        viewModelScope.launch {
            getOtherUserDataUseCase(userId).collectLatest { result ->
                val parsedResult = otherUserMyPageMapper.mapToPresentation(result)
                _postList.value = parsedResult.otherUser.userPosting.sortedByDescending { running ->
                    running.gatheringTime
                }
                postSize.value = _postList.value.size
            }
        }
    }

    fun updateTargetNickname(nickname: String) {
        targetNickname.value = nickname
    }
}