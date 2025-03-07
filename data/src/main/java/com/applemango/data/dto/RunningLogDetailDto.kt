package com.applemango.data.dto

import com.applemango.domain.entity.MemberStamp
import com.applemango.domain.entity.RunningLogDetail
import com.squareup.moshi.Json

data class RunningLogDetailDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: RunningLogDetailData,
)

data class RunningLogDetailData(
    @Json(name = "detailRunningLog") val runningLog: RunningLogDetail,
    @Json(name = "gatheringCount") val gatheringCount: Int,
    @Json(name = "gotStamp") val gotStamp: List<MemberStamp>
)