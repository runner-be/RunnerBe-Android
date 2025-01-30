package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.mapper.PostingDetailMapper
import com.applemango.runnerbe.presentation.mapper.UserMapper
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.UserModel
import com.applemango.runnerbe.presentation.model.type.Pace
import com.applemango.runnerbe.usecaseImpl.user.GetUserDataUseCase
import com.applemango.runnerbe.usecaseImpl.post.DeletePostUseCase
import com.applemango.runnerbe.usecaseImpl.post.GetPostDetailUseCase
import com.applemango.runnerbe.usecaseImpl.post.ApplyPostUseCase
import com.applemango.runnerbe.usecaseImpl.post.ClosePostUseCase
import com.applemango.runnerbe.usecaseImpl.post.ReportPostUseCase
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.GetUserPaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val closePostUseCase: ClosePostUseCase,
    private val applyPostUseCase: ApplyPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserPaceUseCase: GetUserPaceUseCase,
    private val userMapper: UserMapper,
    private val postingDetailMapper: PostingDetailMapper,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val resources = context.resources

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private var isApplyComplete: Boolean = false //내가 신청한 모임이면 true
    val post: MutableLiveData<PostingModel> = MutableLiveData()
    var roomId: Int? = null
    val waitingInfo: ObservableArrayList<UserModel> = ObservableArrayList()
    val runnerInfoList: MutableStateFlow<List<UserModel>> = MutableStateFlow(emptyList())
    private val _processUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val processUiState get() = _processUiState

    private val _dropUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val dropUiState: StateFlow<UiState> get() = _dropUiState

    private val _reportUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val reportUiState: StateFlow<UiState> get() = _reportUiState

    private val _actions: MutableSharedFlow<PostDetailAction> = MutableSharedFlow()
    val actions: SharedFlow<PostDetailAction> get() = _actions

    val locationInfo: MutableStateFlow<String> =
        MutableStateFlow(resources.getString(R.string.no_location_info))

    val isMyPost: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getJoinRunnerCount(joinRunnerCount: Int, maxRunnerCount: Int) =
        "(${joinRunnerCount}/${maxRunnerCount})"

    fun fetchUserId() {
        viewModelScope.launch {
            val id = getUserIdUseCase()
            _userId.value = id
            isMyPost.value = (id != -1)
        }
    }

    fun getPostDetail(postId: Int) = viewModelScope.launch {
        getPostDetailUseCase(postId).collect {
            val userId = userId.value
            val result = postingDetailMapper.mapToPresentation(it)
            isApplyComplete = run {
                result.waitingRunnerInfo?.any { user -> user.userId == userId } == true ||
                       result.runnerInfo?.any { user -> user.userId == userId } == true
            }
            post.value = result.postList.first()
            runnerInfoList.value = result.runnerInfo ?: emptyList()
            waitingInfo.clear()
            waitingInfo.addAll(result.waitingRunnerInfo ?: emptyList())
            roomId = result.roomId
        }
    }

    fun bottomProcess() = viewModelScope.launch {
        val postId = post.value?.postId
        if (postId != null) {
            if (userId.value > 0) {
                val result = if (isMyPost.value) closePostUseCase(postId)
                else applyPostUseCase(postId)
                if (result.isSuccess) {
                    _processUiState.emit(UiState.Success(result.code))
                } else {
                    _processUiState.emit(UiState.Failed(result.message ?: ""))
                }
            } else _processUiState.emit(UiState.Failed("로그인이 필요합니다."))
        } else _processUiState.emit(UiState.AnonymousFailed())
    }

    suspend fun getUserPace(): Pace? {
        return Pace.getPaceByName(getUserPaceUseCase())
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

    fun acceptUserApply() {
        viewModelScope.launch {
            _actions.emit(
                PostDetailAction.ShowTwoBtnDialog(
                    titleText = resources.getString(if (isMyPost.value) R.string.question_post_close else R.string.question_post_apply),
                    firstBtnText = resources.getString(R.string.no),
                    secondBtnText = resources.getString(R.string.yes),
                    firstEvent = {},
                    secondEvent = { bottomProcess() }
                )
            )
        }
    }

    fun reportBtnClicked() {
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

    fun setBottomButtonText(posting: PostingModel): Int {
        return if (isPostClose()) R.string.post_close_complete
        else {
            if (isMyPost.value) R.string.do_post_close
            else {
                if (isApplyComplete) R.string.apply_complete
                else R.string.do_post_apply
            }
        }
    }

    fun isBottomButtonEnabled(whetherEnd: String): Boolean {
        return if (whetherEnd == "Y") false
        else {
            if (isMyPost.value) true
            else !isApplyComplete
        }
    }

    private fun dropPost() = viewModelScope.launch {
        post.value?.postId?.let { postId ->
            val result = deletePostUseCase(postId)
            if (result.isSuccess) {
                _dropUiState.emit(UiState.Success(result.code))
            } else {
                _dropUiState.emit(UiState.Failed(result.message ?: ""))
            }
        } ?: run {
            _dropUiState.emit(UiState.Failed("앱 재실행 후 다시 시도해 주세요."))
        }
    }

    private fun reportPost() = viewModelScope.launch {
        if (userId.value > 0) {
            post.value?.postId?.let { postId ->
                val result = reportPostUseCase(postId)
                if (result.isSuccess) {
                    _reportUiState.emit(UiState.Success(result.code))
                } else {
                    _reportUiState.emit(UiState.Failed(result.message ?: ""))
                }
            }
        }
    }

    fun isParticipatePostIn(posting: PostingModel): Boolean =
        runnerInfoList.value.firstOrNull { it.userId == posting.userId!! } != null

    private fun isPostClose(): Boolean = post.value?.whetherEnd == "Y"

    fun isWaitingUserExist(waitingList: ArrayList<UserModel>): Boolean =
        isMyPost.value && waitingList.size > 0

    fun ageString(posting: PostingModel): String {
        val age = posting.age
        var result = resources.getString(R.string.all_age)
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