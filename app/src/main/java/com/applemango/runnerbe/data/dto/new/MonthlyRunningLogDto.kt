package com.applemango.runnerbe.data.dto.new

import com.applemango.runnerbe.entity.GatheringData
import com.applemango.runnerbe.entity.RunningLog
import com.applemango.runnerbe.entity.TotalCount
import com.squareup.moshi.Json

data class MonthlyRunningLogDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "totalCount") val totalCount: TotalCount?,
    @Json(name = "runningLog") val runningLog: List<RunningLog>,
    @Json(name = "gatheringDays") val gatheringDays: List<GatheringData>,
)