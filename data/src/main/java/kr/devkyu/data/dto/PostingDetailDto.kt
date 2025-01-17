package kr.devkyu.data.dto

import com.squareup.moshi.Json

data class PostingDetailDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : kr.devkyu.data.dto.PostingDetail
)

data class PostingDetail(
    val postList : List<kr.devkyu.data.dto.PostingDto>,
    val runnerInfo : List<kr.devkyu.data.dto.UserDto>?,
    val waitingRunnerInfo : List<kr.devkyu.data.dto.UserDto>?,
    val roomId: Int
)