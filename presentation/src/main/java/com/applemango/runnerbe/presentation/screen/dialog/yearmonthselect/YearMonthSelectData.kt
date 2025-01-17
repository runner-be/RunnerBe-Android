package com.applemango.runnerbe.presentation.screen.dialog.yearmonthselect

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YearMonthSelectData(
    val year: String,
    val month: String
) : Parcelable {

    fun getFullYearMonth() = "$year $month"

    companion object {
        @SuppressLint("NewApi")
        fun getDefaultYearMonthData() : YearMonthSelectData {
            val calendar = Calendar.getInstance()
            val (year, month) = calendar.run {
                Pair(get(Calendar.YEAR).toString(), get(Calendar.MONTH).toString())
            }
            return YearMonthSelectData(year, month)
        }
    }
}
