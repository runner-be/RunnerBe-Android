package com.applemango.runnerbe.entity

data class SocialLoginEntity(
    val login: Login
)

data class Login(
    // 비회원
    val uuid: String?,
    // 회원
    val userId: Int?,
    val jwt: String?,
    val message: String
)