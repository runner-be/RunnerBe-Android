package com.applemango.runnerbe.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * author : 두루
 */

fun parseLocalDateToKorean(localDate: LocalDate): String {
    return "${localDate.year}년 ${localDate.monthValue}월 ${localDate.dayOfMonth}일 ${
        localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    }"
}

fun parseKoreanDateToLocalDate(koreanDate: String): LocalDate {
    val regex = Regex("""(\d{4})년 (\d{1,2})월 (\d{1,2})일""")
    val matchResult = regex.find(koreanDate)

    val (year, month, day) = matchResult?.destructured
        ?: throw IllegalArgumentException("날짜 형식이 잘못되었습니다.")

    return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
}

fun timeHourAndMinute(dateString: String): String {
    val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val to: Date = stringToDate.parse(dateString.replace("T", " ").replace("Z", " "))
    val dateToString = SimpleDateFormat("HH:mm")
    return dateToString.format(to)
}

fun TimeHourAndMinuteAndSecond(dateString: String): String {
    val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val to: Date = stringToDate.parse(dateString)
    val dateToString = SimpleDateFormat("HH:mm:ss")
    return dateToString.format(to)
}

fun DateString(dateString: String): String {
    val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val to: Date = stringToDate.parse(dateString)
    val dateToString = SimpleDateFormat("yyyy-MM-dd")
    return dateToString.format(to)
}

fun dateStringToString(dateString: String, format: SimpleDateFormat): String? {
    val temp = dateString.replace("T", " ").replace("Z", " ")
    val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
    val to: Date? = stringToDate.parse(temp)
    return to?.let { format.format(it) }
}

fun DateStringInT(dateString: String): String = dateString.substring(0, dateString.indexOf("T"))

fun dateStringToLongTime(dateString: ZonedDateTime): Long {
    return dateString.toInstant().toEpochMilli()
}

fun timeStringToLongTime(timeString: String): Long {
    val timeSplit = timeString.split(":")
    return ((timeSplit[0].toInt() * 60 * 60 * 1000) + (timeSplit[1].toInt() * 60 * 1000)).toLong()
}

fun TimeString(dateString: String): String {
    val res = dateString.split(":")
    val hourString = if (res[0] != "00") "${res[0]}시 " else ""
    val minuteString = "${res[1]}분"
    return "$hourString$minuteString"
}

fun BeforeTimeString(dateString: String): String {
    val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = stringToDate.parse(dateString)

    val today = Calendar.getInstance()
    val day = (today.time.time - date.time) / (60 * 60 * 24 * 1000)
    val hour = (today.time.time - date.time) / (60 * 60 * 1000)
    val minute = (today.time.time - date.time) / (60 * 1000)
    return if (day > 0) {
        day.toString() + "일 전"
    } else if (hour > 0) {
        hour.toString() + "시간 전"
    } else {
        minute.toString() + "분 전"
    }
}

fun removeLastNchars(str: String, n: Int): String {
    return if (str == "" || str.length < n) {
        str
    } else str.substring(0, str.length - n)
}

fun getDateList(range: Int): List<String> {
    val format = SimpleDateFormat("M/d (E)")
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return IntRange(0, range).map {
        cal.add(Calendar.DATE, 1)
        format.format(cal.time)
    }
}

fun getMonthAndDay(date: String): String {
    return try {
        val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val temp = date.replace("T", " ").replace("Z", " ")
        val to: Date = stringToDate.parse(temp)
        val dateToString = SimpleDateFormat("MM/dd")
        dateToString.format(to)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        ""
    }

}

fun getYearListByDay(range: Int): List<String> {
    val format = SimpleDateFormat("yyyy")
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return IntRange(0, range).map {
        cal.add(Calendar.DATE, 1)
        format.format(cal.time)
    }
}

/**
 * @param
 * untilYear : 몇년 전까지 불러올 것인지(20이면 20년 전부터 가져옴)
 * range: untilYear ~ range만큼 가져옴
 */
fun getYearListByYear(untilYear: Int, range: Int): List<String> {
    val format = SimpleDateFormat("yyyy")
    val cal = Calendar.getInstance()
    cal.add(Calendar.YEAR, 1 - untilYear)
    return IntRange(0, range).map {
        cal.add(Calendar.YEAR, -1)
        format.format(cal.time)
    }
}

fun getNowYear() = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)