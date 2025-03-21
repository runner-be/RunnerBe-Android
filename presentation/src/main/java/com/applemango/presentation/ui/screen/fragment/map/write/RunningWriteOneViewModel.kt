package com.applemango.presentation.ui.screen.fragment.map.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.presentation.ui.model.vo.PlaceData
import com.applemango.presentation.ui.screen.dialog.dateselect.DateSelectData
import com.applemango.presentation.ui.screen.dialog.timeselect.TimeSelectData
import com.applemango.presentation.R
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
    val runningPlaceInfo : MutableStateFlow<PlaceData?> = MutableStateFlow(null)

    val onNext = combine(runningTitle, runningDisplayTime, runningPlaceInfo) { title, time, address ->
        title.replace("\\s".toRegex(), "").isNotEmpty()
                && (time.hour.toInt() + time.minute.toInt()) > 0
                && address != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = false
    )

    var coordinate = LatLng(0.0, 0.0)

    fun updateAddress(location: AddressData) {
        coordinate = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
        runningPlaceInfo.value = PlaceData(
            location.placeName,
            location.roadAddress,
            location.detailAddress
        )
    }
}