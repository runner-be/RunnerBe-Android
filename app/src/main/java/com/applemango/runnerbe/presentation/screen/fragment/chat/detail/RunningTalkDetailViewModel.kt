package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.loader.content.CursorLoader
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Messages
import com.applemango.runnerbe.data.dto.RoomInfo
import com.applemango.runnerbe.data.network.response.RunningTalkDetailResponse
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.domain.usecase.runningtalk.GetRunningTalkDetailUseCase
import com.applemango.runnerbe.domain.usecase.runningtalk.MessageReportUseCase
import com.applemango.runnerbe.domain.usecase.runningtalk.MessageSendUseCase
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.mapper.RunningTalkDetailMapper
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class RunningTalkDetailViewModel @Inject constructor(
    private val runningTalkDetailUseCase: GetRunningTalkDetailUseCase,
    private val messageSendUseCase: MessageSendUseCase,
    private val messageReportUseCase: MessageReportUseCase
) : ViewModel() {

    val actions: MutableSharedFlow<RunningTalkDetailAction> = MutableSharedFlow()
    var roomId: Int? = null
    var roomRepName: String = ""
    val roomInfo: MutableStateFlow<RoomInfo> =
        MutableStateFlow(RoomInfo("러닝 제목", Pace.BEGINNER.time))
    val messageList: ArrayList<Messages> = ArrayList()
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

    fun getDetailData(isRefresh: Boolean): Job = viewModelScope.launch {
        roomId?.let { roomId ->
            runningTalkDetailUseCase(roomId).collectLatest {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        if (it.body is RunningTalkDetailResponse) {
                            if (isRefresh) messageList.clear()
                            roomInfo.emit(it.body.result.roomInfo[0])
                            messageList.addAll(it.body.result.messages)
                            talkList.value =
                                RunningTalkDetailMapper.parseMessagesToRunningTalkUiState(it.body.result.messages)
                        }
                    }

                    is CommonResponse.Failed -> {}

                    else -> {

                    }
                }
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
                messageSendUseCase(roomId, content, null)
            } else null

            handleResults(textResult, imageResults, content)
        }
    }

    private suspend fun handleResults(
        textResult: CommonResponse?,
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

        when (textResult) {
            is CommonResponse.Success<*> -> {
                _messageSendUiState.emit(UiState.Success(textResult.code))
            }
            is CommonResponse.Failed -> {
                message.value = content
                _messageSendUiState.emit(UiState.Failed(textResult.message))
            }
            null -> {
                if (successImageList.isNotEmpty()) {
                    _messageSendUiState.emit(UiState.Success(200))
                } else {
                    _messageSendUiState.emit(UiState.Empty)
                }
            }
            else -> {}
        }
    }

    fun imageAttachClicked() {
        viewModelScope.launch {
            if (attachImageUrls.value.size < maxImageCount) {
                actions.emit(RunningTalkDetailAction.ShowImageSelect)
            } else {
                actions.emit(
                    RunningTalkDetailAction.ShowToast(
                        RunnerBeApplication.instance.getString(R.string.image_count_alert)
                    )
                )
            }
        }
    }

    // firebase storage 에 이미지 업로드하는 method
    private suspend fun uploadImg(roomId: Int, uri: String, primaryKey: Int): String? {
        return try {
            val name = RunnerBeApplication.mTokenPreference.getUserId()
            val fileName = "$name${Calendar.getInstance().time}${primaryKey}_.png"
            val reference: StorageReference = Firebase.storage.reference.child("item").child(fileName)

            val inputStream = RunnerBeApplication.instance.contentResolver.openInputStream(Uri.parse(uri))
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
                when (messageSendUseCase(roomId, null, path)) {
                    is CommonResponse.Success<*> -> {
                        successImageList.add(path)
                        path
                    }
                    is CommonResponse.Failed -> {
                        failedImageList.add(path)
                        null
                    }
                    else -> null
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
            messageReportUseCase(messageIdList).collect {
                when (it) {
                    is CommonResponse.Success<*> -> {
                        _messageReportUiState.emit(UiState.Success(it.code))
                    }

                    is CommonResponse.Failed -> {
                        _messageReportUiState.emit(UiState.Failed(it.message))
                    }

                    else -> {
                        Log.e(this.javaClass.name, "messageReport - when - else - CommonResponse")
                    }
                }
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