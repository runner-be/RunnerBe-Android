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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun getDetailData(isRefresh: Boolean) {
        viewModelScope.launch {
            roomId?.let { roomId ->
                runningTalkDetailUseCase(roomId).collect {
                    if (it is CommonResponse.Success<*> && it.body is RunningTalkDetailResponse) {
                        if (isRefresh) messageList.clear()
                        roomInfo.emit(it.body.result.roomInfo[0])
                        messageList.addAll(it.body.result.messages)
                        talkList.value =
                            RunningTalkDetailMapper.parseMessagesToRunningTalkUiState(it.body.result.messages)
                    }
                }
            }
        }
    }

    fun sendMessage(content: String) = viewModelScope.launch(Dispatchers.IO) {
        failedImageList.clear()
        successImageList.clear()

        roomId?.let {
            _messageReportUiState.emit(UiState.Loading)

            val uploadImageResults =  attachImageUrls.value.mapIndexed { index, url ->
                async {
                    uploadImg(it, url, index)
                }
            }.awaitAll()

            uploadImageResults.forEachIndexed{ index, isSuccess ->
                val iImage = attachImageUrls.value[index]
                if (isSuccess)
                    successImageList.add(iImage)
                else
                    failedImageList.add(iImage)
            }

            val isImageSend = attachImageUrls.value.size == successImageList.size
            attachImageUrls.value = failedImageList

            if (content.isNotEmpty()) {
                when (val response = messageSendUseCase(it, content, null)) {
                    is CommonResponse.Success<*> -> {
                        _messageSendUiState.emit(UiState.Success(response.code))
                    }

                    is CommonResponse.Failed -> {
                        message.value = content
                        _messageSendUiState.emit(UiState.Failed(response.message))
                    }

                    is CommonResponse.Loading, is CommonResponse.Empty -> {}
                }
            } else _messageSendUiState.emit(if (isImageSend) UiState.Success(200) else UiState.Empty)
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
    private suspend fun uploadImg(roomId: Int, uri: String, primaryKey: Int): Boolean {
        return try {
            val name = RunnerBeApplication.mTokenPreference.getUserId()
            val fileName = "$name${Calendar.getInstance().time}${primaryKey}_.png"
            val reference: StorageReference = Firebase.storage.reference.child("item").child(fileName)

            val inputStream = RunnerBeApplication.instance.contentResolver.openInputStream(Uri.parse(uri))
                ?: throw IllegalArgumentException("Cannot open InputStream for URI: $uri")

            val uploadTask = reference.putStream(inputStream)

            suspendCoroutine<Boolean> { continuation ->
                uploadTask.addOnSuccessListener {
                    downloadUri(roomId, reference, uri)
                    continuation.resume(true)
                }.addOnFailureListener {
                    it.printStackTrace()
                    continuation.resume(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun downloadUri(roomId: Int, reference: StorageReference, originUrl: String) {
//        지정한 경로(reference)에 대한 uri 을 다운로드하는 method
        reference.downloadUrl.addOnSuccessListener {
            viewModelScope.launch(Dispatchers.IO) {
                it?.let { path ->
                    when (messageSendUseCase(roomId, null, path.toString())) {
                        is CommonResponse.Success<*> -> {
                            successImageList.add(path.toString())
                        }

                        is CommonResponse.Failed -> {
                            failedImageList.add(path.toString())
                        }

                        is CommonResponse.Empty, is CommonResponse.Loading -> {}
                    }
                } ?: run { failedImageList.add(originUrl) }
            }
        }.addOnFailureListener {
            it.printStackTrace()
            failedImageList.add(originUrl)
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