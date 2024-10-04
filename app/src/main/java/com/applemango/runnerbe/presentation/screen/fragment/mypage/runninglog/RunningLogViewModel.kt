package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.domain.usecase.runninglog.PostRunningLogUseCase
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.parseLocalDateToKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RunningLogViewModel @Inject constructor(
    private val postRunningLogUseCase: PostRunningLogUseCase
) : ViewModel() {
    private val logId = MutableStateFlow<Int?>(null)
    private val gatheringId = MutableStateFlow<Int?>(null)
    private val logDate = MutableStateFlow<String?>(null)
    val logStrDate: StateFlow<String> = logDate.map { date ->
        date?.let {
            parseLocalDateToKorean(LocalDate.parse(date))
        } ?: ""
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val logType = MutableStateFlow(RunningLogType.ALONE)
    private val logDiary = MutableStateFlow("")
    private val logImage = MutableStateFlow<String?>(null)
    val logStamp = MutableStateFlow(StampItem.unavailableStampItem)
    val logDegree = MutableStateFlow<String?>("")
    val logWeather = MutableStateFlow(
        WeatherItem(
            "WEA000",
            R.drawable.ic_weather_default,
            "기본"
        )
    )
    private val logTeam = MutableStateFlow<String?>(null)
    val logVisibility = MutableStateFlow(true)

    suspend fun postRunningLog(
        userId: Int,
    ): Pair<Boolean, String?> {
        val runningLog = try {
            RunningLogRequest(
                logDate.value ?: throw IllegalArgumentException("Date error"),
                logStamp.value.code,
                logDiary.value,
                logDegree.value?.toInt() ?: throw IllegalArgumentException("기온을 입력해주세요"),
                logWeather.value.code,
                if (logVisibility.value) 1 else 0
            ).also {
                Log.e("postRunningLog", it.toString())
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return Pair(false, e.message)
        }

        var success = false
        val date = LocalDate.parse(logDate.value)
        postRunningLogUseCase(
            userId,
            date.year,
            date.monthValue,
            runningLog
        ).collect { response ->
            success = response is CommonResponse.Success<*> && response.code != 999
            // TODO() 러닝 통계 조회 및 표시
        }

        return Pair(success, null)
    }

    fun updateLogId(logId: Int) {
        this.logId.value = logId
    }

    fun updateGatheringId(logId: Int) {
        this.gatheringId.value = logId
    }

    fun updateLogDate(date: String?) {
        logDate.value = date
    }

    fun updateLogType(type: RunningLogType) {
        logType.value = type
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
        logVisibility.value = !visibility
    }

    override fun onCleared() {
        Log.e("RunningLogViewModel", "onCleared")
        super.onCleared()
    }
}