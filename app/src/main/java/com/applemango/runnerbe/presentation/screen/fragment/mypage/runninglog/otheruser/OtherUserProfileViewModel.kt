package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUser
import com.applemango.runnerbe.data.network.response.OtherUserInfo
import com.applemango.runnerbe.data.network.response.RunningLog
import com.applemango.runnerbe.domain.usecase.runninglog.GetOtherUserProfileUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OtherUserProfileViewModel @Inject constructor(
    private val getOtherUserProfileUseCase: GetOtherUserProfileUseCase
): ViewModel() {
    private val _currentWeeklyViewPagerPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentWeeklyViewPagerPosition: StateFlow<Int?> get() = _currentWeeklyViewPagerPosition.asStateFlow()

    private val date = LocalDate.now()
    val today: String = "${date.year}년 ${date.monthValue}월"

    private val _userInfo = MutableStateFlow<OtherUserInfo?>(null)
    val userInfo: StateFlow<OtherUserInfo?> = _userInfo.asStateFlow()

    private val _userJoinedPosting = MutableStateFlow<List<Posting>>(emptyList())
    val userJoinedPosting: StateFlow<List<Posting>> = _userJoinedPosting.asStateFlow()

    fun getOtherUserProfile(userId: Int) {
        viewModelScope.launch {
            getOtherUserProfileUseCase(userId)
                .collectLatest { response ->
                    when(response) {
                        is CommonResponse.Success<*> -> {
                            val result = response.body as? OtherUser
                            _userJoinedPosting.value = result?.userPosting ?: emptyList()
                            _userInfo.value = result?.userInfo
                        }

                        else -> {
                            throw Exception("")
                        }
                    }
                }
        }
    }

    fun updateWeeklyViewPagerPosition(position: Int) {
        _currentWeeklyViewPagerPosition.value = position
    }
}