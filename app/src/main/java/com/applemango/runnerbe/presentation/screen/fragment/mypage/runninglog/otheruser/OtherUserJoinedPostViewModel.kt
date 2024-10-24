package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.lifecycle.ViewModel
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUser
import com.applemango.runnerbe.data.network.response.OtherUserPosting
import com.applemango.runnerbe.data.network.response.UserDataResponse
import com.applemango.runnerbe.domain.usecase.GetUserDataUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.GetOtherUserProfileUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OtherUserJoinedPostViewModel @Inject constructor(
    private val getOtherUserProfileUseCase: GetOtherUserProfileUseCase
): ViewModel() {
    private val targetUserId = MutableStateFlow<Int?>(null)
    val targetNickname = MutableStateFlow<String?>(null)
    val postSize = MutableStateFlow<Int>(0)

    @OptIn(FlowPreview::class)
    val targetJoinedPostFlow: Flow<List<OtherUserPosting>> = targetUserId
        .filterNotNull()
        .flatMapMerge { userId ->
            getOtherUserProfileUseCase(userId).map { response ->
                when (response) {
                    is CommonResponse.Success<*> -> {
                        val result = response.body as? OtherUser
                        (result?.userPosting ?: emptyList()).also {
                            postSize.value = it.size
                        }
                    }

                    else -> {
                        emptyList()
                    }
                }
            }
        }

    fun updateTargetUserId(userId: Int) {
        targetUserId.value = userId
    }

    fun updateTargetNickname(nickname: String) {
        targetNickname.value = nickname
    }
}