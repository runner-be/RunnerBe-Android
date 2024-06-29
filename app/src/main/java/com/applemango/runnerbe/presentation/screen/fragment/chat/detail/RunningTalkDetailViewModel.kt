package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.preview.RunningTalkDetailImageSelectListener
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.mapper.RunningTalkDetailMapper
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import javax.inject.Inject


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

    fun getDetailData(isRefresh: Boolean) = viewModelScope.launch {
        roomId?.let { roomId ->
            runningTalkDetailUseCase(roomId).collect {
                if (it is CommonResponse.Success<*> && it.body is RunningTalkDetailResponse) {
                    if (isRefresh) messageList.clear()
                    roomInfo.emit(it.body.result.roomInfo[0])
                    messageList.addAll(it.body.result.messages)
                    talkList.value =
                        RunningTalkDetailMapper.messagesToRunningTalkUiState(it.body.result.messages)
                }
            }
        }
    }

    fun messageSend(content: String) = viewModelScope.launch(Dispatchers.IO) {
        failedImageList.clear()
        successImageList.clear()
        roomId?.let {
            message.value = ""
            _messageReportUiState.emit(UiState.Loading)

            attachImageUrls.value.forEachIndexed { index, url -> uploadImg(it, url, index) }
            //다 끝날때까지 대기
            while (attachImageUrls.value.size != successImageList.size + failedImageList.size) {
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

    //        firebase storage 에 이미지 업로드하는 method
    private fun uploadImg(roomId: Int, uri: String, primaryKey: Int) {
        var uploadTask: UploadTask? = null // 파일 업로드하는 객체
        val name = RunnerBeApplication.mTokenPreference.getUserId()
        val fileName = "$name${Calendar.getInstance().time}${primaryKey}_.png"
        val reference: StorageReference = Firebase.storage.reference.child("item")
            .child(fileName) // 이미지 파일 경로 지정 (/item/imageFileName)
        uploadTask = uri.let { reference.putFile(Uri.fromFile(File(uri))) } // 업로드할 파일과 업로드할 위치 설정
        uploadTask.addOnSuccessListener {
            downloadUri(roomId, reference, uri) // 업로드 성공 시 업로드한 파일 Uri 다운받기
        }.addOnFailureListener {
            it.printStackTrace()
            failedImageList.add(uri)
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
        attachImageUrls.value = ArrayList(attachImageUrls.value).apply {
            getRealPath(uri)?.let {
                add(it)
            }
        }
    }

    private fun getRealPath(uri: Uri): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return if (isMediaStoreUri(uri)) {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                val loader = CursorLoader(
                    RunnerBeApplication.instance.applicationContext,
                    uri,
                    proj,
                    null,
                    null,
                    null
                )
                loader.loadInBackground()?.let { cursor ->
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val result = cursor.getString(columnIndex)
                    cursor.close()
                    result
                }
            } else uri.toString()
        } else uri.path
    }

    private fun isMediaStoreUri(uri: Uri): Boolean {
        return uri.scheme.equals(
            "content",
            ignoreCase = true
        ) && MediaStore.AUTHORITY == uri.authority
    }

    fun getImageSelectListener() = object : RunningTalkDetailImageSelectListener {
        override fun imageDeleteClick(position: Int) {
            attachImageUrls.value = ArrayList(attachImageUrls.value).apply {
                this.removeAt(position)
            }
        }
    }

    fun getTalkClickListener() = object : RunningTalkDetailListClickListener {
        override fun imageClicked(imageUrl: String, talkIdList: List<Int>, clickItemId: Int) {
            val images =
                messageList.filter { talkIdList.contains(it.messageId) && it.imageUrl != null }
            val i = images.indexOfFirst { it.messageId == clickItemId }
            val index = if (i < 0) 0 else i
            viewModelScope.launch {
                actions.emit(
                    RunningTalkDetailAction.MoveToImageDetail(
                        clickPageNumber = index,
                        images = images.map { it.imageUrl ?: "" },
                        title = images[index].nickName
                    )
                )
            }
        }
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