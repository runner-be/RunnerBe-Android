package com.applemango.runnerbe.entity

import java.time.ZonedDateTime

data class MonthlyRunningLogEntity (
    val totalCount: TotalCount?,
    val runningLog: List<RunningLog>,
    val gatheringDays: List<GatheringData>,
): BaseEntity()

data class GatheringData (
    val gatheringId: Int,
    val date: ZonedDateTime
)

data class TotalCount(
    val groupRunningCount: Int,
    val personalRunningCount: Int
)

data class RunningLog(
    val logId: Int?,
    var gatheringId: Int?,
    val runnedDate: ZonedDateTime,
    val stampCode: String?,
    val isOpened: Int,
)