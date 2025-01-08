package kr.devkyu.domain.entity

import java.time.ZonedDateTime

data class AlarmEntity(
    val alarmId: Int,
    val userId: Int,
    val createdAt: ZonedDateTime,
    val title: String,
    val content: String,
    val whetherRead: String,
): BaseEntity()