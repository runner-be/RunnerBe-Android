package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.usecaseImpl.runninglog.GetJoinedRunnersUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.GetRunningLogDetailUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteRunningLogUseCase
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import com.applemango.runnerbe.util.LogUtil
import com.applemango.runnerbe.util.parseKoreanDateToLocalDate
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
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
    private val getJoinedRunnersUseCase: GetJoinedRunnersUseCase,
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase
) : ViewModel() {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val postedRunningLogFlow = logId
        .filterNotNull()
        .flatMapLatest {
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            getRunningLogDetailUseCase(userId, it)
        }.flowOn(Dispatchers.IO)

    suspend fun postRunningLog(
        userId: Int,
    ): Pair<Boolean, String?> {
        val imageUrl = if (logImage.value != null) {
            uploadImg(logImage.value.toString())
        } else {
            null
        }

        val runningLog = try {
            val degree = logDegree.value?.replace("+", "0")?.toInt()
            RunningLogParam(
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
                userId,
                date.year,
                date.monthValue,
                runningLog
            )
            Pair(result.isSuccess, null)
        } else {
            val result = updateRunningLogUseCase(
                userId,
                logId,
                runningLog
            )
            Pair(result.isSuccess, null)
        }
    }

    private suspend fun uploadImg(uri: String): String? {
        return try {
            val name = RunnerBeApplication.mTokenPreference.getUserId()
            val fileName = "${name}_running_log_${Calendar.getInstance().time}png"
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
                val userId = RunnerBeApplication.mTokenPreference.getUserId()
                getJoinedRunnersUseCase(userId, gId).catch {
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