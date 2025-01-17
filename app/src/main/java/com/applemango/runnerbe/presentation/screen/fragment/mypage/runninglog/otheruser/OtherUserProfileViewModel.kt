package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.OtherUserMyPageMapper
import com.applemango.runnerbe.presentation.mapper.PostingMapper
import com.applemango.runnerbe.presentation.model.OtherUserInfo
import com.applemango.runnerbe.presentation.model.OtherUserMyPageModel
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.usecaseImpl.user.GetOtherUserDataUseCase
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
    private val getOtherUserDataUseCase: GetOtherUserDataUseCase,
    private val otherUserMyPageMapper: OtherUserMyPageMapper,
    private val postingMapper: PostingMapper,
) : ViewModel() {
    private val _currentWeeklyViewPagerPosition: MutableStateFlow<Int> = MutableStateFlow(2)
    val currentWeeklyViewPagerPosition: StateFlow<Int> get() = _currentWeeklyViewPagerPosition.asStateFlow()

    private val _viewpagerRunningCount: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(
        Pair(0,0)
    )
    val viewpagerRunningCount: StateFlow<Pair<Int, Int>> get() = _viewpagerRunningCount

    private val date = LocalDate.now()
    val today: String = "${date.year}년 ${date.monthValue}월"

    private val _otherUserInfo = MutableStateFlow<OtherUserInfo?>(null)
    val otherUserInfo: StateFlow<OtherUserInfo?> = _otherUserInfo.asStateFlow()

    private val _userJoinedPosting = MutableStateFlow<List<PostingModel>>(emptyList())
    val userJoinedPosting: StateFlow<List<PostingModel>> = _userJoinedPosting.asStateFlow()

    fun getOtherUserProfile(userId: Int) {
        viewModelScope.launch {
            getOtherUserDataUseCase(userId)
                .collectLatest { result ->
                    val parsedResult = otherUserMyPageMapper.mapToPresentation(result)
                    _userJoinedPosting.value =
                        parsedResult.otherUser.userPosting.sortedByDescending { running ->
                            running.gatheringTime
                        } ?: emptyList()
                    _otherUserInfo.value = parsedResult.otherUser.userInfo
                }
        }
    }

    fun updateWeeklyViewPagerPosition(position: Int) {
        _currentWeeklyViewPagerPosition.value = position
    }

    fun addViewPagerCounts(groupCount: Int, personalCount: Int) {
        _viewpagerRunningCount.value = Pair(groupCount, personalCount)
    }
}