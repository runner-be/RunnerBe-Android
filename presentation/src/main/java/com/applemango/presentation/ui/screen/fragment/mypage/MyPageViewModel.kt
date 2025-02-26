package com.applemango.presentation.ui.screen.fragment.mypage

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.runninglog.GetMonthlyRunningLogsUseCase
import com.applemango.domain.usecaseImpl.user.GetUserDataUseCase
import com.applemango.domain.usecaseImpl.user.UpdateUserImageUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.domain.usecaseImpl.user.local.UpdateUserLocalPaceUseCase
import com.applemango.presentation.ui.mapper.MonthlyRunningLogMapper
import com.applemango.presentation.ui.mapper.PostingMapper
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.model.MonthlyRunningLogsModel
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.ui.state.UiState
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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val getMonthlyRunningLogsUseCase: GetMonthlyRunningLogsUseCase,
    private val updateUserLocalPaceUseCase: UpdateUserLocalPaceUseCase,
    private val monthlyRunningLogMapper: MonthlyRunningLogMapper,
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper,
) : ViewModel() {
    private val _userId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val userId: StateFlow<Int?> get() = _userId.asStateFlow()

    private val _currentMondayYearMonth: MutableStateFlow<String> =
        MutableStateFlow("${LocalDate.now().year}년 ${LocalDate.now().monthValue}월")
    val currentMondayYearMonth: StateFlow<String> get() = _currentMondayYearMonth.asStateFlow()

    private val _currentWeeklyViewPagerPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentWeeklyViewPagerPosition: StateFlow<Int?> get() = _currentWeeklyViewPagerPosition.asStateFlow()

    private val _viewpagerRunningCount: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(
        Pair(0, 0)
    )
    val viewpagerRunningCount: StateFlow<Pair<Int, Int>> get() = _viewpagerRunningCount

    val userInfo: MutableStateFlow<UserModel?> = MutableStateFlow(null)
    val diligence: MutableStateFlow<String?> = MutableStateFlow(null)
    val pace: MutableStateFlow<String?> = MutableStateFlow(null)
    val joinPosts = MutableStateFlow<List<PostingModel>>(emptyList())
    val myPosts: ObservableArrayList<PostingModel> = ObservableArrayList()
    val moveTab: MutableSharedFlow<Int> = MutableSharedFlow()

    private var _updateUserImageState: MutableLiveData<UiState> = MutableLiveData()
    val updateUserImageState get() = _updateUserImageState

    private val _actions = MutableSharedFlow<MyPageAction>()
    val actions get() = _actions

    private val _runningLogResult = MutableStateFlow<Map<Int, MonthlyRunningLogsModel>>(emptyMap())
    val runningLogResult: StateFlow<Map<Int, MonthlyRunningLogsModel>> get() = _runningLogResult.asStateFlow()

    fun fetchUserId() {
        viewModelScope.launch {
            _userId.value = getUserIdUseCase()
        }
    }

    /**
     * >> 총 21개의 데이터가 보여야하므로 <<
     * 이전 달의 데이터가 필요한 경우 -> 오늘 날짜가 21일 미만이라면
     * 이번 달의 데이터만 필요한 경우 -> 오늘 날짜가 21일 이상이라면
     */
    fun fetchUserRunningLog(
        today: LocalDate,
        targetUserId: Int?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = targetUserId ?: getUserIdUseCase()
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
                        monthlyRunningLogMapper.mapToPresentation(it)
                    }
                    val thisMonthResult = deferredThisMonthResult.await().map {
                        monthlyRunningLogMapper.mapToPresentation(it)
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
                        monthlyRunningLogMapper.mapToPresentation(response)
                    }
                    .collectLatest { result ->
                        _runningLogResult.value = mapOf(Pair(thisMonth, result))
                    }
            }
        }
    }

    fun getUserData() = viewModelScope.launch {
        getUserDataUseCase().collect {
            val userInfo: UserModel? =
                it.userInfo?.let { user -> userMapper.mapToPresentation(user) }
            myPosts.clear()
            joinPosts.value = it.myRunning
                .map { posting ->
                    postingMapper.mapToPresentation(posting)
                }.sortedByDescending { running -> running.gatheringTime }
            it.myPosting
                .map { postingEntity ->
                    postingMapper.mapToPresentation(postingEntity)
                }.let { postingList ->
                    myPosts.addAll(postingList)
                }
            this@MyPageViewModel.userInfo.value = userInfo
            updateUserLocalPaceUseCase(userInfo?.pace ?: "")
            pace.emit(userInfo?.pace)
            diligence.emit(it.userInfo?.diligence ?: "초보 출석")
        }
    }

    fun updatePostBookmark(post: PostingModel) {
        val postList: MutableList<PostingModel> = joinPosts.value.toMutableList()
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
        val result = updateUserImageUseCase(imageUrl)
        if (result.isSuccess) {
            _updateUserImageState.value = UiState.Success(result.code)
        } else {
            _updateUserImageState.value = UiState.Failed(result.message ?: "")
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