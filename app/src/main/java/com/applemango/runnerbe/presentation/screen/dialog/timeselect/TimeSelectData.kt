package com.applemango.runnerbe.presentation.screen.dialog.timeselect

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSelectData(
    val hour: String,
    val minute : String
) : Parcelable {

    fun getFullDisplayTime() = "$hour 시간 ${if(minute.toInt() in 1 .. 9) "0$minute" else minute} 분"
    fun getTransferType() = "$hour:$minute"
    companion object {
        fun getDefaultTimeData() = TimeSelectData(hour = "0", minute = "0")
    }
}
