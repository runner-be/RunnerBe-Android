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
import com.applemango.runnerbe.domain.usecase.GetUserDataUseCase
import com.applemango.runnerbe.domain.usecase.PatchUserImageUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.GetMonthlyRunningLogListUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val patchUserImageUseCase: PatchUserImageUseCase,
    private val getMonthlyRunningLogListUseCase: GetMonthlyRunningLogListUseCase
) : ViewModel() {
    private val _currentWeeklyViewPagerPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentWeeklyViewPagerPosition: StateFlow<Int?> get() = _currentWeeklyViewPagerPosition.asStateFlow()

    val userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val diligence: MutableStateFlow<String?> = MutableStateFlow(null)
    val pace: MutableStateFlow<String?> = MutableStateFlow(null)
    val joinPosts = MutableStateFlow<List<Posting>>(emptyList())
    val myPosts: ObservableArrayList<Posting> = ObservableArrayList()
    val moveTab : MutableSharedFlow<Int> = MutableSharedFlow()

    private val _isShowInfoDialog: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private var _updateUserImageState : MutableLiveData<UiState> = MutableLiveData()
    val updateUserImageState get() = _updateUserImageState

    private val _actions = MutableSharedFlow<MyPageAction>()
    val actions get() = _actions

    private val todayDateFlow = MutableStateFlow<LocalDate>(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val thisWeekRunningLogFlow: Flow<RunningLogResult> = todayDateFlow
        .flatMapLatest { today ->
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            val (todayYear, todayMonth)  = Pair(today.year, today.monthValue)
            getMonthlyRunningLogListUseCase(userId, todayYear, todayMonth)
                .map { response ->
                    when (response) {
                        is CommonResponse.Success<*> -> {
                            response.body as RunningLogResult
                        }

                        is CommonResponse.Failed -> {
                            throw Exception(response.message)
                        }

                        else -> {
                            throw Exception("MyPageViewModel-thisWeekRunningLogFlow-when-else")
                        }
                    }
                }
        }.catch { error ->
            error.printStackTrace()
        }
        .flowOn(Dispatchers.IO)

    fun getUserData(userId: Int) = viewModelScope.launch {
        if (userId > -1) {
            getUserDataUseCase(userId).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        if (it.body is UserDataResponse) {
                            val result = it.body.result
                            val userInfo = result.userInfo
                            myPosts.clear()
                            joinPosts.value = result.myRunning
                            result.myPosting?.let { postingList ->
                                myPosts.addAll(postingList)
                            }
                            this@MyPageViewModel.userInfo.value = userInfo
                            RunnerBeApplication.mTokenPreference.setMyRunningPace(userInfo.pace ?: "")
                            pace.emit(userInfo.pace)
                            diligence.emit(result.userInfo.diligence ?: "초보 출석")
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


    fun userProfileImageChange(imageUrl : String?) = viewModelScope.launch {
        patchUserImageUseCase(imageUrl).collect {
            _updateUserImageState.postValue(
                when(it) {
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

    fun setTab(index : Int) = viewModelScope.launch { moveTab.emit(index) }

    fun paceRegistrationClicked() = viewModelScope.launch {
        _actions.emit(MyPageAction.MoveToPaceRegistration)
    }

    fun updateWeeklyViewPagerPosition(position: Int) {
        _currentWeeklyViewPagerPosition.value = position
    }
}

sealed class MyPageAction {
    object MoveToPaceRegistration : MyPageAction()
}