package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

// 회원탈퇴
data class DeleteUser(
    @Json(name = "deleted userId") val deletedUserId: Int
)