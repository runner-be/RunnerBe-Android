package kr.devkyu.data.dto

import com.squareup.moshi.Json

data class CommonDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null
)