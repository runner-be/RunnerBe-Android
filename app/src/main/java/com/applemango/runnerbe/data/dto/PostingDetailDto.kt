package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

data class PostingDetailDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : PostingDetail
)

data class PostingDetail(
    val postList : List<PostingDto>,
    val runnerInfo : List<UserDto>?,
    val waitingRunnerInfo : List<UserDto>?,
    val roomId: Int
)