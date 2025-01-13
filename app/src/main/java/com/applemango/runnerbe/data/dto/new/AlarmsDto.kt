package com.applemango.runnerbe.data.dto.new

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

data class AlarmsDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val alarms: List<Alarm>
)

/**
 * @param createdAt 알림이 생성된 시간
 * @param whetherRead Y / N 으로 나뉘며, 읽은 알림은 N
 */
@JsonClass(generateAdapter = true)
data class Alarm(
    @Json(name = "alarmId") val alarmId: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "createdAt") val createdAt: ZonedDateTime,
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "whetherRead") val whetherRead: String,
)