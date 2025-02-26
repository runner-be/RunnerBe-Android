package com.applemango.presentation.ui.screen.fragment.mypage.calendar

import com.applemango.presentation.ui.model.RunningLogModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

data class DateItem(
    val date: LocalDate?,
    val runningLog: RunningLogModel?
)

fun initWeekDays(thisWeekMonday: LocalDate, runningLogList: List<RunningLogModel>): List<DateItem> {
    return (0 .. 6).map { day ->
        val date = thisWeekMonday.plusDays(day.toLong())
        DateItem(
            date,
            runningLogList.firstOrNull { log ->
                log.runnedDate.toLocalDate() == date
            }
        )
    }
}

fun initYearMonthDays(year: Int, month: Int, runningLogList: List<RunningLogModel>): List<DateItem> {
    val currYearMonth = YearMonth.of(year, Month.of(month))
    val firstDayOfMonth = currYearMonth.atDay(1)
    val emptyDayOfWeekCount = (firstDayOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value).coerceAtLeast(0)

    val emptyDays = (1 .. emptyDayOfWeekCount).map {
        DateItem(null, null)
    }
    val monthlyDays = (1 .. currYearMonth.lengthOfMonth()).map { day ->
        val tempToday = LocalDate.of(year, Month.of(month), day)
        val tempRunningLog = runningLogList.firstOrNull {
            it.runnedDate.toLocalDate() == tempToday
        }
        DateItem(
            date = tempToday,
            runningLog = tempRunningLog
        )
    }
    return (emptyDays + monthlyDays)
        .sortedBy { it.date }
}