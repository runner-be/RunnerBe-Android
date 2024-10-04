package com.applemango.runnerbe.presentation.screen.fragment.map.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.vo.RunningWriteTransferData
import com.applemango.runnerbe.domain.usecase.post.WriteRunningUseCase
import com.applemango.runnerbe.presentation.model.GenderTag
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceSelectItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.initPaceInfoList
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RunningWriteTwoViewModel @Inject constructor(
    private val writeUseCase: WriteRunningUseCase
) : ViewModel() {
    val paceList: MutableStateFlow<List<PaceSelectItem>> = MutableStateFlow(initPaceInfoList())
    val oneData: MutableStateFlow<RunningWriteTransferData> = MutableStateFlow(
        RunningWriteTransferData(
            runningTitle = "",
            runningDate = Calendar.getInstance().time,
            runningDisplayDate = DateSelectData.defaultNowDisplayDate(),
            runningDisplayTime = TimeSelectData.getDefaultTimeData(),
            runningTag = RunningTag.All,
            coordinate = LatLng(0.0, 0.0)
        )
    )

    private val _writeSate: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val writeState get() = _writeSate

    val genderRadioChecked: MutableStateFlow<Int> = MutableStateFlow(R.id.allTab)
    val afterPartyRadioChecked: MutableStateFlow<Int> = MutableStateFlow(R.id.hasNotExistTab)
    val content: MutableStateFlow<String> = MutableStateFlow("")
    val joinRunnerCount: MutableStateFlow<Int> = MutableStateFlow(2)
    val isAllAgeChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val recruitmentStartAge: MutableStateFlow<Int> = MutableStateFlow(20)
    val recruitmentEndAge: MutableStateFlow<Int> = MutableStateFlow(40)
    val locationInfo: MutableStateFlow<String> =
        MutableStateFlow(RunnerBeApplication.instance.applicationContext.getString(R.string.no_location_info))
    val isConfirmButtonEnabled = combine(paceList) { data ->
        data[0].any { it.isSelected }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(1000L),initialValue = false)
    val recruitmentAge = combine(recruitmentStartAge, recruitmentEndAge) { start, end ->
        RunnerBeApplication.ApplicationContext().resources.getString(
            R.string.display_recruitment_age_setting,
            start.toString(),
            end.toString()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = ""
    )

    fun joinRunnerPlus() {
        joinRunnerCount.value = joinRunnerCount.value + 1
    }

    fun joinRunnerMinus() {
        joinRunnerCount.value = joinRunnerCount.value - 1
    }

    fun getPaceInfoSelectListener() : PaceSelectListener = object : PaceSelectListener {
        override fun itemClick(paceSelectItem: PaceSelectItem) {
            val list = ArrayList<PaceSelectItem>().apply {
                addAll(paceList.value.map { item ->
                    item.copy().apply { isSelected = this.pace == paceSelectItem.pace }
                })
            }
            paceList.value = list
        }
    }

    fun writeRunning(userId: Int) = viewModelScope.launch {
        writeUseCase(userId, WriteRunningRequest(
            runningTitle = oneData.value.runningTitle,
            runningTag = oneData.value.runningTag.tag,
            gatheringTime = SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(oneData.value.runningDate),
            runningTime = oneData.value.runningDisplayTime.getTransferType(),
            numberOfRunner = joinRunnerCount.value,
            gender = when (genderRadioChecked.value) {
                R.id.maleTab -> GenderTag.MALE
                R.id.femaleTab -> GenderTag.FEMALE
                else -> GenderTag.ALL
            }.tag,
            minAge = if (isAllAgeChecked.value) 10 else recruitmentStartAge.value,
            maxAge = if (isAllAgeChecked.value) 100 else recruitmentEndAge.value,
            latitude = oneData.value.coordinate.latitude,
            longitude = oneData.value.coordinate.longitude,
            locationInfo = locationInfo.value,
            contents = content.value.ifEmpty { null },
            paceGrade = paceList.value.firstOrNull{ it.isSelected }?.pace?.key?:"",
            isAfterParty = when(afterPartyRadioChecked.value) {
                R.id.hasExistTab -> 1
                else -> 2
            }
        )).collect {
            _writeSate.emit(
                when (it) {
                    is CommonResponse.Success<*> -> {
                        if (it.body is BaseResponse) {
                            if (it.body.isSuccess) UiState.Success(it.code)
                            else UiState.Failed(it.body.message ?: "error")
                        } else UiState.Failed("서버에 문제가 발생했습니다.")
                    }
                    is CommonResponse.Failed -> UiState.Failed(it.message)
                    is CommonResponse.Loading -> UiState.Loading
                    else -> UiState.Empty
                }
            )
        }

    }
}