package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar

import com.applemango.runnerbe.data.network.response.RunningLog
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

data class DateItem(
    val date: LocalDate?,
    val runningLog: RunningLog?
)

fun initWeekDays(runningLogList: List<RunningLog>): List<DateItem> {
    val thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    return (0 .. 6).map { day ->
        val date = thisMonday.plusDays(day.toLong())
        DateItem(
            date,
            runningLogList.firstOrNull { log ->
                log.runnedDate.toLocalDate() == date
            }
        )
    }
}

fun initYearMonthDays(year: Int, month: Int, runningLogList: List<RunningLog>): List<DateItem> {
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