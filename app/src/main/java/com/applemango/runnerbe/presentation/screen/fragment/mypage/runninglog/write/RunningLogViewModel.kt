package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import com.applemango.runnerbe.domain.usecase.runninglog.GetRunningLogDetailUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.PatchRunningLogUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.PostRunningLogUseCase
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.parseKoreanDateToLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RunningLogViewModel @Inject constructor(
    private val postRunningLogUseCase: PostRunningLogUseCase,
    private val patchRunningLogUseCase: PatchRunningLogUseCase,
    private val getRunningLogDetailUseCase: GetRunningLogDetailUseCase
) : ViewModel() {
    private val logId = MutableStateFlow<Int?>(null)
    val gatheringId = MutableStateFlow<Int?>(null)
    val logDate = MutableStateFlow("")
    val logType = MutableStateFlow(RunningLogType.ALONE)
    val logDiary = MutableStateFlow("")
    val logImage = MutableStateFlow<Uri?>(null)
    val logStamp = MutableStateFlow(StampItem.unavailableStampItem)
    val logDegree = MutableStateFlow<String?>("-")
    val logWeather = MutableStateFlow(
        WeatherItem(
            "WEA000",
            R.drawable.ic_weather_default,
            "기본"
        )
    )
    private val logTeam = MutableStateFlow<String?>(null)
    val logVisibility = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val postedRunningLogFlow = logId
        .filterNotNull()
        .flatMapLatest {
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            getRunningLogDetailUseCase(userId, it).map { response ->
                when(response) {
                    is CommonResponse.Success<*> -> {
                        response.body as? DetailRunningLogResponse
                    }

                    is CommonResponse.Failed -> {
                        throw Exception(response.message)
                    }

                    else -> {
                        throw Exception("RunningLogDetailViewModel-runningLogDetailFlow-when-else")
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    suspend fun postRunningLog(
        userId: Int,
    ): Pair<Boolean, String?> {
        val runningLog = try {
            val degree = logDegree.value?.replace("+","0")?.toInt()
            RunningLogRequest(
                parseKoreanDateToLocalDate(logDate.value).toString(),
                logStamp.value.code,
                gatheringId.value,
                logDiary.value,
                degree,
                logWeather.value.code,
                if (logVisibility.value) 1 else 0
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return Pair(false, e.message)
        }

        var success = false
        val date = parseKoreanDateToLocalDate(logDate.value)
        val logId = logId.value

        if (logId == null) {
            postRunningLogUseCase(
                userId,
                date.year,
                date.monthValue,
                runningLog
            ).collect { response ->
                success = response is CommonResponse.Success<*> && response.code != 999
                // TODO() 러닝 통계 조회 및 표시
            }
        } else {
            patchRunningLogUseCase(
                userId,
                logId,
                runningLog
            ).collect { response ->
                success = response is CommonResponse.Success<*> && response.code != 999
                // TODO() 러닝 통계 조회 및 표시
            }
        }

        return Pair(success, null)
    }

    fun updateLogId(logId: Int?) {
        this.logId.value = logId
    }

    fun updateGatheringId(logId: Int?) {
        this.gatheringId.value = logId
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