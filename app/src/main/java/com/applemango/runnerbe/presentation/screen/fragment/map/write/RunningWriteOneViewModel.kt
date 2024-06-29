package com.applemango.runnerbe.presentation.screen.fragment.map.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.*

class RunningWriteOneViewModel : ViewModel() {

    val radioChecked : MutableStateFlow<Int> = MutableStateFlow(R.id.beforeTab)
    val runningTitle : MutableStateFlow<String> = MutableStateFlow("")
    var runningDate : Date = Calendar.getInstance().time
    val runningDisplayDate : MutableStateFlow<DateSelectData> = MutableStateFlow(DateSelectData.defaultNowDisplayDate())
    val runningDisplayTime : MutableStateFlow<TimeSelectData> = MutableStateFlow(TimeSelectData.getDefaultTimeData())

    val onNext = combine(runningTitle, runningDisplayTime) { title, time ->
        title.replace("\\s".toRegex(), "").isNotEmpty() && (time.hour.toInt() + time.minute.toInt()) > 0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = false
    )

    var coordinate = LatLng(0.0, 0.0)
}