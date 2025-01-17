package com.applemango.runnerbe.presentation.model

import java.time.ZonedDateTime

data class RunningLogModel(
    val logId: Int?,
    var gatheringId: Int?,
    val runnedDate: ZonedDateTime,
    val stampCode: String?,
    val isOpened: Int,
)