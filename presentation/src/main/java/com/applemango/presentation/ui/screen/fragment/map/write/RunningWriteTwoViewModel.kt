package com.applemango.presentation.ui.screen.fragment.map.write

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.post.WritePostUseCase
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.ui.model.type.GenderTag
import com.applemango.presentation.ui.model.type.RunningTag
import com.applemango.presentation.ui.model.vo.RunningWriteTransferData
import com.applemango.presentation.ui.screen.dialog.dateselect.DateSelectData
import com.applemango.presentation.ui.screen.dialog.timeselect.TimeSelectData
import com.applemango.presentation.ui.screen.fragment.mypage.paceinfo.PaceInfoProvider
import com.applemango.presentation.ui.screen.fragment.mypage.paceinfo.PaceSelectItem
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.R
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RunningWriteTwoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    paceInfoProvider: PaceInfoProvider,
    private val writeUseCase: WritePostUseCase
) : ViewModel() {
    private val resources = context.resources

    val paceList: MutableStateFlow<List<PaceSelectItem>> = MutableStateFlow(paceInfoProvider.initPaceInfoList())
    val oneData: MutableStateFlow<RunningWriteTransferData> = MutableStateFlow(
        RunningWriteTransferData(
            runningTitle = "",
            runningDate = Calendar.getInstance().time,
            runningDisplayDate = DateSelectData.defaultNowDisplayDate(),
            runningDisplayTime = TimeSelectData.getDefaultTimeData(),
            runningTag = RunningTag.All,
            coordinate = LatLng(0.0, 0.0),
            placeData = null
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
    val isConfirmButtonEnabled = combine(paceList) { data ->
        data[0].any { it.isSelected }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(1000L),initialValue = false)
    val recruitmentAge = combine(recruitmentStartAge, recruitmentEndAge) { start, end ->
        resources.getString(
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

    fun writeRunning() = viewModelScope.launch {
        val placedata = oneData.value.placeData
        val result = writeUseCase(
            WritePostUseCase.WriteRunningParam(
                runningTitle = oneData.value.runningTitle,
                runningTag = oneData.value.runningTag.tag,
                gatheringTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oneData.value.runningDate),
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
                placeName = checkNotNull(placedata?.placeName),
                placeAddress = checkNotNull(placedata?.placeAddress),
                placeExplain = checkNotNull(placedata?.placeExplain),
                contents = content.value.ifEmpty { null },
                paceGrade = paceList.value.firstOrNull { it.isSelected }?.pace?.key ?: "",
                isAfterParty = when (afterPartyRadioChecked.value) {
                    R.id.hasExistTab -> 1
                    else -> 2
                }
            )
        )

        if (result.isSuccess) {
            _writeSate.emit(UiState.Success(result.code))
        } else {
            _writeSate.emit(UiState.Failed(result.message ?: ""))
        }
    }
}