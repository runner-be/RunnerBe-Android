package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class GetNotificationsResponse(
    val alarms: List<Alarm>
): BaseResponse()

/**
 * @param createdAt 알림이 생성된 시간
 * @param whetherRead Y / N 으로 나뉘며, 읽은 알림은 N
 */
data class Alarm(
    @SerializedName("alarmId") val alarmId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("createdAt") val createdAt: ZonedDateTime,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("whetherRead") val whetherRead: String,
)