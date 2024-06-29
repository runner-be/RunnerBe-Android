package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.domain.usecase.post.*
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val postClosingUseCase: PostClosingUseCase,
    private val postApplyUseCase: PostApplyUseCase,
    private val dropPostUseCase: DropPostUseCase,
    private val postReportUseCase: PostReportUseCase
) :
    ViewModel() {

    private var isApplyComplete: Boolean = false //내가 신청한 모임이면 true
    val post: MutableLiveData<Posting> = MutableLiveData()
    var roomId: Int? = null
    val waitingInfo: ObservableArrayList<UserInfo> = ObservableArrayList()
    val runnerInfo: ObservableArrayList<UserInfo> = ObservableArrayList()
    private val _processUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val processUiState get() = _processUiState

    private val _dropUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val dropUiState: StateFlow<UiState> get() = _dropUiState

    private val _reportUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val reportUiState: StateFlow<UiState> get() = _reportUiState

    private val _actions: MutableSharedFlow<PostDetailAction> = MutableSharedFlow()
    val actions: SharedFlow<PostDetailAction> get() = _actions

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
                            is CommonResponse.Failed -> UiState.Failed(it.message)
                            is CommonResponse.Loading -> UiState.Loading
                            else -> UiState.Empty
                        }
                    )
                }
            } else _processUiState.emit(UiState.Failed("로그인이 필요합니다."))
        } else _processUiState.emit(UiState.AnonymousFailed())
    }

    fun backClicked() {
        viewModelScope.launch {
            _actions.emit(PostDetailAction.MoveToBack)
        }
    }

    fun waitingBtnClicked() {
        viewModelScope.launch {
            _actions.emit(PostDetailAction.ShowAppliedRunnerListDialog)
        }
    }

    fun bottomBtnClicked() {
        val resources = RunnerBeApplication.instance.resources
        viewModelScope.launch {
            _actions.emit(PostDetailAction.ShowTwoBtnDialog(
                titleText = resources.getString(if (isMyPost()) R.string.question_post_close else R.string.question_post_apply),
                firstBtnText = resources.getString(R.string.no),
                secondBtnText = resources.getString(R.string.yes),
                firstEvent = {},
                secondEvent = { bottomProcess() }
            )
            )
        }
    }

    fun reportBtnClicked() {
        val resources = RunnerBeApplication.instance.resources
        viewModelScope.launch {
            _actions.emit(PostDetailAction.ShowTwoBtnDialog(
                titleText = resources.getString(R.string.msg_warning_report),
                firstBtnText = resources.getString(R.string.yes),
                secondBtnText = resources.getString(R.string.no),
                firstEvent = { reportPost() },
                secondEvent = {}
            )
            )
        }
    }

    fun moreIconClicked() {
        val resources = RunnerBeApplication.instance.resources
        viewModelScope.launch {
            _actions.emit(PostDetailAction.ShowSelectListDialog(
                listOf(
                    SelectItemParameter(resources.getString(R.string.do_delete)) {
                        dropPost()
                    }
                )
            ))
        }
    }
    fun moveToMessageClicked() {
        viewModelScope.launch {
            val id = roomId ?: return@launch
            val name = post.value?.nickName ?: return@launch
            _actions.emit(PostDetailAction.MoveToMessage(id, name))
        }
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

    private fun dropPost() = viewModelScope.launch {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId > 0) {
            post.value?.postId?.let { postId ->
                dropPostUseCase(postId, userId).collect {
                    _dropUiState.emit(
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
            } ?: run {
                _dropUiState.emit(UiState.Failed("앱 재실행 후 다시 시도해 주세요."))
            }
        } else _dropUiState.emit(UiState.Failed("로그인이 필요합니다."))
    }

    private fun reportPost() = viewModelScope.launch {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId > 0) {
            post.value?.postId?.let { postId ->
                postReportUseCase(postId, userId).collect {
                    _reportUiState.emit(
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
        }
    }

    fun isParticipatePostIn(posting: Posting): Boolean =
        runnerInfo.any { it.userId == RunnerBeApplication.mTokenPreference.getUserId() }

    private fun isPostClose(): Boolean = post.value?.whetherEnd == "Y"
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

sealed class PostDetailAction() {
    object MoveToBack : PostDetailAction()
    object ShowAppliedRunnerListDialog : PostDetailAction()
    data class MoveToMessage(val roomId: Int, val nickName: String) : PostDetailAction()
    data class ShowTwoBtnDialog(
        val titleText: String,
        val firstBtnText: String,
        val secondBtnText: String,
        val firstEvent: () -> Unit,
        val secondEvent: () -> Unit,
    ) : PostDetailAction()
    data class ShowSelectListDialog(
        val list: List<SelectItemParameter>
    ) : PostDetailAction()
}