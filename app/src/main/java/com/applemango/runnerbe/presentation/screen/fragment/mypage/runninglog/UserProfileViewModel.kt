package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import androidx.lifecycle.ViewModel
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUser
import com.applemango.runnerbe.data.network.response.OtherUserInfo
import com.applemango.runnerbe.data.network.response.RunningLog
import com.applemango.runnerbe.domain.usecase.runninglog.GetOtherUserProfileUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getOtherUserProfileUseCase: GetOtherUserProfileUseCase
): ViewModel() {
    private val _targetUserIdFlow = MutableStateFlow<Int?>(null)
    val targetUserIdFlow : StateFlow<Int?> get() = _targetUserIdFlow.asStateFlow()

    private val date = LocalDate.now()
    val today: String = "${date.year}년 ${date.monthValue}월"

    private val _userInfo = MutableStateFlow<OtherUserInfo?>(null)
    val userInfo: StateFlow<OtherUserInfo?> = _userInfo.asStateFlow()

    private val _userRunningLogs = MutableStateFlow<List<RunningLog>>(emptyList())
    val userRunningLogs: StateFlow<List<RunningLog>> = _userRunningLogs.asStateFlow()

    private val _userPostings = MutableStateFlow<List<Posting>>(emptyList())
    val userPostings: StateFlow<List<Posting>> = _userPostings.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val otherUserProfileFlow: Flow<String> = _targetUserIdFlow
        .filterNotNull()
        .flatMapLatest { id ->
            getOtherUserProfileUseCase(id)
                .map { response ->
                    when(response) {
                        is CommonResponse.Success<*> -> {
                            val result = response.body as? OtherUser
                            _userInfo.value = result?.userInfo
                            _userRunningLogs.value = result?.userLogInfo ?: emptyList()
                            _userPostings.value = result?.userPosting ?: emptyList()

                            LogUtil.errorLog(result.toString())
                            "Success"
                        }

                        else -> {
                            throw Exception("")
                        }
                    }
                }
        }

    fun updateTargetUserId(targetUserId: Int) {
        _targetUserIdFlow.value = targetUserId
    }
}