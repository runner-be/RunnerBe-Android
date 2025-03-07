package com.applemango.presentation.ui.model

import java.time.ZonedDateTime

data class AlarmModel(
    val alarmId: Int,
    val userId: Int,
    val createdAt: ZonedDateTime,
    val title: String,
    val content: String,
    val whetherRead: String,
)