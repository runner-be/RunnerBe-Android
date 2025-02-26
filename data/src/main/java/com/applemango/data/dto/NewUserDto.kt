package com.applemango.data.dto

import com.squareup.moshi.Json

data class NewUserDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: Register
)

data class Register(
    @Json(name = "insertedUserId") val insertedUserId: Int,
    @Json(name = "token") val token: String
)