package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class MyPageDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : GetMyPageResult
)

@JsonClass(generateAdapter = true)
data class GetMyPageResult(
    @Json(name = "myInfo") val userInfo: UserDto?,
    @Json(name = "myPosting") val myPosting: List<PostingDto>,
    @Json(name = "myRunning") val myRunning: List<PostingDto>
)