package com.applemango.runnerbe.presentation.screen.dialog.dateselect

import android.os.Parcelable
import com.applemango.runnerbe.presentation.model.RunningTag
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar

@Parcelize
data class DateSelectData(
    val formatDate: String,
    val AMAndPM: String,
    val hour: String,
    val minute: String
) : Parcelable {
    fun getFullDisplayDate() = "$formatDate $AMAndPM $hour:$minute"

    companion object {
        fun defaultNowDisplayDate(): DateSelectData {
            val now = Calendar.getInstance().time
            val nowString = SimpleDateFormat("M/d (E)-k-mm").format(now).split("-")
            val hour = nowString[1]
            return DateSelectData(
                formatDate = nowString[0],
                AMAndPM = if(hour.toInt() in 12 ..23) "PM" else "AM",
                hour = if(hour.toInt() >= 24) "0" else if (hour.toInt() <= 12) hour else "${hour.toInt() - 12}",
                minute = "${nowString[2].toInt() / 10}0"
            )
        }

        fun runningTagDefault(runningTag: RunningTag) : DateSelectData {
            val now = Calendar.getInstance().time
            val date = SimpleDateFormat("M/d (E)").format(now)
            return when(runningTag) {
                RunningTag.After -> {
                    DateSelectData(
                        formatDate = date,
                        AMAndPM = "PM",
                        hour = "6",
                        minute = "00"
                    )
                }
                RunningTag.Holiday -> {
                    DateSelectData(
                        formatDate = date,
                        AMAndPM = "PM",
                        hour = "12",
                        minute = "00"
                    )
                }
                else -> {
                    DateSelectData(
                        formatDate = date,
                        AMAndPM = "AM",
                        hour = "6",
                        minute = "00"
                    )
                }
            }
        }
    }
}