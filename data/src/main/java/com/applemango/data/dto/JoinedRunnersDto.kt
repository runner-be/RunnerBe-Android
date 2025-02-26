package com.applemango.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class JoinedRunnersResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: List<JoinedRunnerDto>
)

@JsonClass(generateAdapter = true)
data class JoinedRunnerDto(
    @Json(name = "userId") val userId: Int,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    @Json(name = "logId") val logId: String?,
    @Json(name = "isOpened") val isOpened: Int?,
    @Json(name = "stampCode") var stampCode: String?,
    @Json(name = "isCaptain") val isCaptain: Int,
)