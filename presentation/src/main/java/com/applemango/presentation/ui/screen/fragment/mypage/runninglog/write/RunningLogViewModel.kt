package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.write

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.runninglog.GetJoinedRunnersUseCase
import com.applemango.domain.usecaseImpl.runninglog.GetRunningLogDetailUseCase
import com.applemango.domain.usecaseImpl.runninglog.UpdateRunningLogUseCase
import com.applemango.domain.usecaseImpl.runninglog.WriteRunningLogUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.presentation.ui.mapper.RunningLogDetailMapper
import com.applemango.presentation.ui.model.RunningLogDetailModel
import com.applemango.presentation.ui.screen.dialog.stamp.StampItem
import com.applemango.presentation.ui.screen.dialog.weather.WeatherItem
import com.applemango.presentation.util.parseKoreanDateToLocalDate
import com.applemango.presentation.R
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.util.parseLocalDateToKorean
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.storage.StorageReference as StorageReference

@HiltViewModel
class RunningLogViewModel @Inject constructor(
    private val writeRunningLogUseCase: WriteRunningLogUseCase,
    private val updateRunningLogUseCase: UpdateRunningLogUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getJoinedRunnersUseCase: GetJoinedRunnersUseCase,
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase,
    private val runningLogDetailMapper: RunningLogDetailMapper,
    private val contentResolver: ContentResolver
) : ViewModel() {
    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private val logId = MutableStateFlow<Int?>(null)
    val gatheringId = MutableStateFlow<Int?>(null)
    val logDate = MutableStateFlow("")
    val logType = MutableStateFlow(RunningLogType.ALONE)
    val logDiary = MutableStateFlow("")
    val logImage = MutableStateFlow<Uri?>(null)
    val logStamp: MutableStateFlow<StampItem?> = MutableStateFlow(null)
    val logDegree = MutableStateFlow<String?>("-")
    val logWeather = MutableStateFlow(
        WeatherItem(
            "WEA000",
            R.drawable.ic_weather_default,
            "기본"
        )
    )
    val logVisibility = MutableStateFlow(true)
    val joinedRunnerSize = MutableStateFlow(0)

    private val _runningLogData = MutableStateFlow<RunningLogDetailModel?>(null)
    val runningLogData: StateFlow<RunningLogDetailModel?> get() = _runningLogData

    fun getPostedRunningLog(
        logId: Int
    ) {
        viewModelScope.launch {
            val userId = getUserIdUseCase()
            val result = getRunningLogDetailUseCase(userId, logId).map {
                runningLogDetailMapper.mapToPresentation(it)
            }
            result.collect { log ->
                _runningLogData.value = log
                updateLogDate(parseLocalDateToKorean(log.runningLog.runnedDate.toLocalDate()))
                updateLogDiary(log.runningLog.contents)
                updateLogVisibility(log.runningLog.isOpened == 1)
            }
        }
    }

    suspend fun postRunningLog(): Pair<Boolean, String?> {
        val imageUrl = if (logImage.value != null) {
            uploadImg(logImage.value.toString())
        } else {
            null
        }

        val runningLog = try {
            val degree = logDegree.value?.replace("+", "0")?.toInt()
            UpdateRunningLogUseCase.RunningLogParam(
                parseKoreanDateToLocalDate(logDate.value).toString(),
                requireNotNull(logStamp.value?.code),
                gatheringId.value,
                logDiary.value,
                imageUrl,
                degree,
                logWeather.value.code,
                if (logVisibility.value) 1 else 0
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return Pair(false, e.message)
        }

        val date = parseKoreanDateToLocalDate(logDate.value)
        val logId = logId.value



        return if (logId == null) {
            val result = writeRunningLogUseCase(
                date.year,
                date.monthValue,
                runningLog
            )
            Pair(result.isSuccess, null)
        } else {
            val result = updateRunningLogUseCase(
                logId,
                runningLog
            )
            Pair(result.isSuccess, null)
        }
    }

    private suspend fun uploadImg(uri: String): String? {
        return try {
            val fileName = "${System.currentTimeMillis()}_running_log_${Calendar.getInstance().time}png"
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
                val downloadSuccess = downloadUri(reference)
                downloadSuccess
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun downloadUri(reference: StorageReference): String? {
        return try {
            suspendCoroutine { continuation ->
                reference.downloadUrl.addOnSuccessListener { uri ->
                    continuation.resume(uri.toString())
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    continuation.resume(null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateLogId(logId: Int?) {
        this.logId.value = logId
    }

    fun updateGatheringId(gatheringId: Int?) {
        gatheringId?.let { gId ->
            this.gatheringId.value = gId
            viewModelScope.launch(Dispatchers.IO) {
                getJoinedRunnersUseCase(gId).catch {
                    it.printStackTrace()
                }.collectLatest { response ->
                    joinedRunnerSize.value = response.size
                }
            }
        }
    }

    fun updateLogDate(date: String) {
        logDate.value = date
    }

    fun updateLogType(type: RunningLogType) {
        logType.value = type
    }

    fun updateLogImage(image: Uri) {
        logImage.value = image
    }

    fun updateStamp(stamp: StampItem) {
        logStamp.value = stamp
    }

    fun updateLogDiary(diary: String) {
        logDiary.value = diary
    }

    fun updateDegreeAndWeather(degree: String, weather: WeatherItem) {
        logDegree.value = degree
        logWeather.value = weather
    }

    fun updateLogVisibility(visibility: Boolean) {
        logVisibility.value = visibility
    }
}