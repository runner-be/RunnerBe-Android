package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.domain.usecase.post.GetPostDetailUseCase
import com.applemango.runnerbe.domain.usecase.post.PostApplyUseCase
import com.applemango.runnerbe.domain.usecase.post.PostClosingUseCase
import com.applemango.runnerbe.domain.usecase.post.PostDetailManufacture
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val postClosingUseCase: PostClosingUseCase,
    private val postApplyUseCase: PostApplyUseCase
) :
    ViewModel() {

    private var isApplyComplete: Boolean = false //내가 신청한 모임이면 true
    val post: MutableLiveData<Posting> = MutableLiveData()
    var roomId: Int? = null
    val waitingInfo: ObservableArrayList<UserInfo> = ObservableArrayList()
    val runnerInfo: ObservableArrayList<UserInfo> = ObservableArrayList()
    private val _processUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val processUiState get() = _processUiState

    val locationInfo: MutableStateFlow<String> =
        MutableStateFlow(RunnerBeApplication.instance.applicationContext.getString(R.string.no_location_info))

    fun getJoinRunnerCount(joinRunnerCount: Int, maxRunnerCount: Int) =
        "(${joinRunnerCount}/${maxRunnerCount})"

    fun getPostDetail(postId: Int, userId: Int) = viewModelScope.launch {
        getPostDetailUseCase(postId, userId).collect {
            if (it is CommonResponse.Success<*> && it.body is PostDetailManufacture) {
                isApplyComplete = it.body.code == 1015
                post.value = it.body.post
                runnerInfo.clear()
                it.body.runnerInfo?.let { runnerList -> runnerInfo.addAll(runnerList) }
                waitingInfo.clear()
                it.body.waitingInfo?.let { waitingList -> waitingInfo.addAll(waitingList) }
                roomId = it.body.roomId
            }
            Log.e("하...", runnerInfo.toString())
        }
    }

    fun bottomProcess() = viewModelScope.launch {
        val postId = post.value?.postId
        if (postId != null) {
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            if (userId > 0) {
                val response = if (isMyPost()) postClosingUseCase(postId)
                else postApplyUseCase(postId, userId)
                response.collect {
                    _processUiState.emit(
                        when (it) {
                            is CommonResponse.Success<*> -> UiState.Success(it.code)
                            is CommonResponse.Failed -> {
                                if (it.code >= 999) UiState.NetworkError
                                else UiState.Failed(it.message)
                            }
                            is CommonResponse.Loading -> UiState.Loading
                            else -> UiState.Empty
                        }
                    )
                }
            } else _processUiState.emit(UiState.Failed("로그인이 필요합니다."))
        } else _processUiState.emit(UiState.AnonymousFailed())
    }

    fun setBottomButtonText(posting: Posting): Int {
        return if (isPostClose()) R.string.post_close_complete
        else {
            if (isMyPost()) R.string.do_post_close
            else {
                if (isApplyComplete) R.string.apply_complete
                else R.string.do_post_apply
            }
        }
    }

    fun isBottomButtonEnabled(whetherEnd: String): Boolean {
        return if (whetherEnd == "Y") false
        else {
            if (isMyPost()) true
            else !isApplyComplete
        }
    }

    fun isParticipatePostIn(posting: Posting): Boolean =
        runnerInfo.any { it.userId == RunnerBeApplication.mTokenPreference.getUserId() }

    fun isPostClose(): Boolean = post.value?.whetherEnd == "Y"
    fun isMyPost(): Boolean =
        RunnerBeApplication.mTokenPreference.getUserId() == post.value?.postUserId

    fun isWaitingUserExist(waitingList: ArrayList<UserInfo>): Boolean =
        isMyPost() && waitingList.size > 0

    fun ageString(posting: Posting): String {
        val age = posting.age
        var result = RunnerBeApplication.instance.resources.getString(R.string.all_age)
        runCatching {
            val ageSplit = age.split("-")
            if (!(ageSplit[0].toInt() < 20 || ageSplit[1].toInt() > 65)) result = age
        }
        return result
    }
}