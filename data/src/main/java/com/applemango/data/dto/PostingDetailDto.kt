package com.applemango.data.dto

import com.squareup.moshi.Json

data class PostingDetailDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : PostingDetail
)

data class PostingDetail(
    @Json(name = "postingInfo") val postList : List<PostingDto>,
    @Json(name = "runnerInfo") val runnerInfo : List<UserDto>?,
    @Json(name = "waitingRunnerInfo") val waitingRunnerInfo : List<UserDto>?,
    @Json(name = "roomId") val roomId: Int,
    @Json(name = "gatheringId") val gatheringId: Int,
)