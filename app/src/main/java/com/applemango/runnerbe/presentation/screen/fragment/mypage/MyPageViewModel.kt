package com.applemango.runnerbe.presentation.screen.fragment.mypage

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.RunningLogResult
import com.applemango.runnerbe.data.network.response.UserDataResponse
import com.applemango.runnerbe.domain.usecase.user.GetUserDataUseCase
import com.applemango.runnerbe.domain.usecase.user.UpdateUserImageUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.GetMonthlyRunningLogsUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val getMonthlyRunningLogsUseCase: GetMonthlyRunningLogsUseCase
) : ViewModel() {
    private val _currentMondayYearMonth: MutableStateFlow<String> = MutableStateFlow("${LocalDate.now().year}년 ${LocalDate.now().monthValue}월")
    val currentMondayYearMonth: StateFlow<String> get() = _currentMondayYearMonth.asStateFlow()

    private val _currentWeeklyViewPagerPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentWeeklyViewPagerPosition: StateFlow<Int?> get() = _currentWeeklyViewPagerPosition.asStateFlow()

    private val _viewpagerRunningCount: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(
        Pair(0,0)
    )
    val viewpagerRunningCount: StateFlow<Pair<Int, Int>> get() = _viewpagerRunningCount

    val userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val diligence: MutableStateFlow<String?> = MutableStateFlow(null)
    val pace: MutableStateFlow<String?> = MutableStateFlow(null)
    val joinPosts = MutableStateFlow<List<Posting>>(emptyList())
    val myPosts: ObservableArrayList<Posting> = ObservableArrayList()
    val moveTab: MutableSharedFlow<Int> = MutableSharedFlow()

    private var _updateUserImageState: MutableLiveData<UiState> = MutableLiveData()
    val updateUserImageState get() = _updateUserImageState

    private val _actions = MutableSharedFlow<MyPageAction>()
    val actions get() = _actions

    private val _runningLogResult = MutableStateFlow<Map<Int, RunningLogResult>>(emptyMap())
    val runningLogResult: StateFlow<Map<Int, RunningLogResult>> get() = _runningLogResult.asStateFlow()

    /**
     * >> 총 21개의 데이터가 보여야하므로 <<
     * 이전 달의 데이터가 필요한 경우 -> 오늘 날짜가 21일 미만이라면
     * 이번 달의 데이터만 필요한 경우 -> 오늘 날짜가 21일 이상이라면
     */
    fun fetchUserRunningLog(
        today: LocalDate,
        userId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val todayDate = today.dayOfMonth
            val thisYear = today.year
            val prevMonth = if (today.monthValue == 1) 12 else today.monthValue - 1
            val thisMonth = today.monthValue

            if (todayDate < 21) {
                val deferredPrevMonthResult = async {
                    getMonthlyRunningLogsUseCase(userId, thisYear, prevMonth)
                }
                val deferredThisMonthResult = async {
                    getMonthlyRunningLogsUseCase(userId, thisYear, thisMonth)
                }

                try {
                    val prevMonthResult = deferredPrevMonthResult.await().map {
                        it.processRunningLogResult()
                    }
                    val thisMonthResult = deferredThisMonthResult.await().map {
                        it.processRunningLogResult()
                    }
                    combine(prevMonthResult, thisMonthResult) { prevMonthData, thisMonthData ->
                        mapOf(
                            Pair(prevMonth, prevMonthData),
                            Pair(thisMonth, thisMonthData)
                        )
                    }.collectLatest { runningLogResults ->
                        _runningLogResult.value = runningLogResults
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                getMonthlyRunningLogsUseCase(userId, thisYear, thisMonth)
                    .map { response ->
                        response.processRunningLogResult()
                    }
                    .collectLatest { result ->
                        _runningLogResult.value = mapOf(Pair(thisMonth, result))
                    }
            }
        }
    }

    private fun CommonResponse.processRunningLogResult() : RunningLogResult {
        return when(this) {
            is CommonResponse.Success<*> -> this.body as RunningLogResult
            is CommonResponse.Failed -> {
                LogUtil.errorLog("${this.code} | " + this.message)
                throw Exception(this.message)
            }
            else -> throw Exception("unexpected response")
        }
    }

    fun getUserData(userId: Int) = viewModelScope.launch {
        if (userId > -1) {
            getUserDataUseCase(userId).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        if (it.body is UserDataResponse) {
                            val result = it.body.result
                            val userInfo: UserInfo? = result.userInfo
                            myPosts.clear()
                            joinPosts.value = result.myRunning.sortedByDescending { running -> running.gatheringTime }
                            result.myPosting?.let { postingList ->
                                myPosts.addAll(postingList)
                            }
                            this@MyPageViewModel.userInfo.value = userInfo
                            RunnerBeApplication.mTokenPreference.setMyRunningPace(
                                userInfo?.pace ?: ""
                            )
                            pace.emit(userInfo?.pace)
                            diligence.emit(result.userInfo?.diligence ?: "초보 출석")
                        }
                    }

                    else -> {
                        Log.e("MyPageViewModel", "getUserData - when - else")
                    }
                }
            }
        } else {
            //에러 메시지 뱉자~
        }
    }

    fun updatePostBookmark(post: Posting) {
        val postList: MutableList<Posting> = joinPosts.value.toMutableList()
        val parsedPostList = postList.map { item ->
            if (item.postId == post.postId) {
                val prevBookmark = if (post.bookMark == 1) 0 else 1
                item.copy(bookMark = prevBookmark)
            } else {
                item
            }
        }
        this.joinPosts.value = parsedPostList
    }


    fun userProfileImageChange(imageUrl: String?) = viewModelScope.launch {
        updateUserImageUseCase(imageUrl).collect {
            _updateUserImageState.postValue(
                when (it) {
                    is CommonResponse.Success<*> -> UiState.Success(it.code)
                    is CommonResponse.Failed -> {
                        if (it.code <= 999) UiState.NetworkError
                        else UiState.Failed(it.message)
                    }

                    is CommonResponse.Loading -> UiState.Loading
                    else -> UiState.Empty
                }
            )
        }
    }

    fun setTab(index: Int) = viewModelScope.launch { moveTab.emit(index) }

    fun paceRegistrationClicked() = viewModelScope.launch {
        _actions.emit(MyPageAction.MoveToPaceRegistration)
    }

    fun updateWeeklyViewPagerPosition(position: Int) {
        _currentWeeklyViewPagerPosition.value = position
    }

    fun addViewPagerCounts(groupCount: Int, personalCount: Int) {
        _viewpagerRunningCount.value = Pair(groupCount, personalCount)
    }

    fun updateCurrentMondayMonth(year: Int, month: Int) {
        _currentMondayYearMonth.value = "${year}년 ${month}월"
    }
}

sealed class MyPageAction {
    object MoveToPaceRegistration : MyPageAction()
}