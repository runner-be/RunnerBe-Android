package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.presentation.mapper.RunningTalkMessageMapper
import com.applemango.runnerbe.presentation.model.RoomInfoModel
import com.applemango.runnerbe.presentation.model.RunningTalkMessageModel
import com.applemango.runnerbe.presentation.model.type.Pace
import com.applemango.runnerbe.usecaseImpl.runningtalk.GetRunningTalkMessagesUseCase
import com.applemango.runnerbe.usecaseImpl.runningtalk.ReportMessageUseCase
import com.applemango.runnerbe.usecaseImpl.runningtalk.SendMessageUseCase
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.mapper.RunningTalkDetailMapper
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.usecaseImpl.user.local.GetUserIdUseCase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class RunningTalkDetailViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val runningTalkDetailUseCase: GetRunningTalkMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val reportMessageUseCase: ReportMessageUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val runningTalkMessageMapper: RunningTalkMessageMapper,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    val actions: MutableSharedFlow<RunningTalkDetailAction> = MutableSharedFlow()
    var roomId: Int? = null
    var roomRepName: String = ""
    val roomInfo: MutableStateFlow<RoomInfoModel> =
        MutableStateFlow(RoomInfoModel("러닝 제목", Pace.BEGINNER.time))
    val messageList: MutableList<RunningTalkMessageModel> = mutableListOf()
    val talkList: MutableStateFlow<List<RunningTalkUiState>> = MutableStateFlow(emptyList())
    val message: MutableStateFlow<String> = MutableStateFlow("")
    val isDeclaration: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _messageSendUiState: MutableSharedFlow<UiState> = MutableSharedFlow()
    val messageSendUiState get() = _messageSendUiState
    private val _messageReportUiState: MutableSharedFlow<UiState> = MutableSharedFlow()
    val messageReportUiState get() = _messageReportUiState

    val attachImageUrls: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val failedImageList = ArrayList<String>()
    private val successImageList = ArrayList<String>()
    private val maxImageCount = 3

    fun fetchUserId() {
        viewModelScope.launch {
            val id = getUserIdUseCase()
            _userId.value = id
        }
    }

    fun getDetailData(isRefresh: Boolean): Job = viewModelScope.launch {
        roomId?.let { roomId ->
            runningTalkDetailUseCase(roomId).collectLatest {
                val roomData= it.roomInfo.map { room ->
                    RoomInfoModel(
                        room.talkTitle,
                        room.pace
                    )
                }
                val messages = it.messages.map { message ->
                    runningTalkMessageMapper.mapToPresentation(message)
                }
                if (isRefresh) messageList.clear()
                roomInfo.emit(roomData[0])
                messageList.addAll(messages)
                talkList.value = RunningTalkDetailMapper.parseMessagesToRunningTalkUiState(messages)
            }
        }
    }

    fun sendMessage(content: String) = viewModelScope.launch(Dispatchers.IO) {
        failedImageList.clear()
        successImageList.clear()

        roomId?.let { roomId ->
            _messageReportUiState.emit(UiState.Loading)

            val imageResults = attachImageUrls.value.mapIndexed { index, url ->
                async { url to (uploadImg(roomId, url, index) != null) }
            }.awaitAll()
            val textResult = if (content.isNotEmpty()) {
                sendMessageUseCase(roomId, content, null)
            } else null

            handleResults(textResult, imageResults, content)
        }
    }

    private suspend fun handleResults(
        textResult: CommonEntity?,
        imageResults: List<Pair<String, Boolean>>,
        content: String
    ) {
        imageResults.forEach { (url, isSuccess) ->
            if (isSuccess)
                successImageList.add(url)
            else
                failedImageList.add(url)
        }
        attachImageUrls.value = failedImageList

        if (textResult?.isSuccess == true) {
            _messageSendUiState.emit(UiState.Success(textResult.code))
        } else {
            message.value = content
            _messageSendUiState.emit(UiState.Failed(textResult?.message ?: ""))
        }
    }

    fun imageAttachClicked() {
        viewModelScope.launch {
            if (attachImageUrls.value.size < maxImageCount) {
                actions.emit(RunningTalkDetailAction.ShowImageSelect)
            } else {
                actions.emit(
                    RunningTalkDetailAction.ShowToast(
                        context.resources.getString(R.string.image_count_alert)
                    )
                )
            }
        }
    }

    // firebase storage 에 이미지 업로드하는 method
    private suspend fun uploadImg(roomId: Int, uri: String, primaryKey: Int): String? {
        return try {
            val id = userId.value
            val fileName = "$id${Calendar.getInstance().time}${primaryKey}_.png"
            val reference: StorageReference = Firebase.storage.reference.child("item").child(fileName)

            val inputStream = contentResolver.openInputStream(Uri.parse(uri))
                ?: throw IllegalArgumentException("Cannot open InputStream for URI: $uri")

            val uploadTask = reference.putStream(inputStream)
            val uploadSuccess = suspendCoroutine<Boolean> { continuation ->
                uploadTask.addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    continuation.resume(false)
                }
            }

            if (uploadSuccess) {
                val downloadSuccess = downloadUri(roomId, reference, uri)
                downloadSuccess
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun downloadUri(roomId: Int, reference: StorageReference, originUrl: String): String? {
        return try {
            val downloadUrl = suspendCoroutine<String?> { continuation ->
                reference.downloadUrl.addOnSuccessListener { uri ->
                    continuation.resume(uri.toString())
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    continuation.resume(null)
                }
            }

            downloadUrl?.let { path ->
                val result = sendMessageUseCase(roomId, null, path)

                if (result.isSuccess) {
                    successImageList.add(path)
                    path
                } else {
                    failedImageList.add(path)
                    null
                }
            } ?: run {
                failedImageList.add(originUrl)
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            failedImageList.add(originUrl)
            null
        }
    }

    fun messageReport() = viewModelScope.launch {
        _messageReportUiState.emit(UiState.Loading)
        val messageCheckList = talkList.value.filter {
            it is RunningTalkUiState.OtherRunningTalkUiState && it.isChecked
        }
        val messageIdList: ArrayList<Int> = arrayListOf()
        messageCheckList.forEach {
            if (it is RunningTalkUiState.OtherRunningTalkUiState) {
                it.items.forEach { item ->
                    messageIdList.add(item.messageId)
                }
            }
        }
        if (messageIdList.isNotEmpty()) {
            val result = reportMessageUseCase(messageIdList)
            if (result.isSuccess) {
                _messageReportUiState.emit(UiState.Success(result.code))
            } else {
                _messageReportUiState.emit(UiState.Failed(result.message ?: ""))
            }
        } else {
            isDeclaration.value = false
            _messageReportUiState.emit(UiState.Empty)
        }
    }

    fun setDeclaration(set: Boolean) {
        talkList.value = talkList.value.map {
            when (it) {
                is RunningTalkUiState.MyRunningTalkUiState -> it
                is RunningTalkUiState.OtherRunningTalkUiState -> {
                    it.copy(isReportMode = set)
                }
            }
        }
        isDeclaration.value = set
    }

    fun selectImage(uri: Uri) {
        val prevList = attachImageUrls.value.toMutableList()
        prevList.add(uri.toString())
        attachImageUrls.value = prevList
    }
}

sealed class RunningTalkDetailAction {
    object ShowImageSelect : RunningTalkDetailAction()
    data class ShowToast(val message: String) : RunningTalkDetailAction()
    data class MoveToImageDetail(
        val title: String,
        val images: List<String>,
        val clickPageNumber: Int
    ) : RunningTalkDetailAction()
}