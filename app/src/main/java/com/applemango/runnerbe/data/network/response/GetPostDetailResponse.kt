package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.PostDetail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPostDetailResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : PostDetail
)
