package com.applemango.data.dto

import com.applemango.domain.entity.Login
import com.squareup.moshi.Json

data class SocialLoginDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: Login
)