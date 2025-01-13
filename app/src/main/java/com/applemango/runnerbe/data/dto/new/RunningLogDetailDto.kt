package com.applemango.runnerbe.data.dto.new

import com.applemango.runnerbe.entity.RunningLogDetail
import com.applemango.runnerbe.entity.MemberStamp
import com.squareup.moshi.Json

data class RunningLogDetailDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "runningLog") val runningLog: RunningLogDetail,
    @Json(name = "gatheringCount") val gatheringCount: Int,
    @Json(name = "gotStamp") val gotStamp: List<MemberStamp>
)