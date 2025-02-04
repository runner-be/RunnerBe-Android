package com.applemango.presentation.ui.model

import java.time.ZonedDateTime

data class MonthlyRunningLogsModel(
    val totalCount: TotalCount?,
    val runningLog: List<RunningLogModel>,
    val gatheringDays: List<GatheringData>,
)

data class GatheringData (
    val gatheringId: Int,
    val date: ZonedDateTime
)

data class TotalCount(
    val groupRunningCount: Int,
    val personalRunningCount: Int
)