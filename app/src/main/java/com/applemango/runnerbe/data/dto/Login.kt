package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

// 카카오 로그인, 네이버 로그인
data class Login(
    // 비회원
    @Json(name = "uuid") val uuid: String?,
    // 회원
    @Json(name = "userId") val userId: Int?,
    @Json(name = "jwt") val jwt: String?,
    @Json(name = "message") val message: String
)