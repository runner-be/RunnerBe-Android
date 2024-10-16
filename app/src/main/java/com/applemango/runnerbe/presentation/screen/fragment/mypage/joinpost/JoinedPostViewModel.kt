package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinpost

import androidx.lifecycle.ViewModel
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.UserDataResponse
import com.applemango.runnerbe.domain.usecase.GetUserDataUseCase
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
class JoinedPostViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase
): ViewModel() {
    private val targetUserId = MutableStateFlow<Int?>(null)
    val targetNickname = MutableStateFlow<String?>(null)
    val postSize = MutableStateFlow<Int>(0)

    @OptIn(FlowPreview::class)
    val targetJoinedPostFlow: Flow<List<Posting>> = targetUserId
        .filterNotNull()
        .flatMapMerge { userId ->
            getUserDataUseCase(userId).map { response ->
                when (response) {
                    is CommonResponse.Success<*> -> {
                        val result = response.body as? UserDataResponse
                        (result?.result?.myRunning ?: emptyList()).also {
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