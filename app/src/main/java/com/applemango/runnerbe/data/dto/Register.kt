package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

// 회원가입
data class Register(
    @Json(name = "insertedUserId") val insertedUserId: Int,
    @Json(name = "token") val token: String
)