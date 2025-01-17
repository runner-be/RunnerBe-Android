package kr.devkyu.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class MyPageDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : kr.devkyu.data.dto.GetMyPageResult
)

@JsonClass(generateAdapter = true)
data class GetMyPageResult(
    @Json(name = "myInfo") val userInfo: kr.devkyu.data.dto.UserDto?,
    @Json(name = "myPosting") val myPosting: List<kr.devkyu.data.dto.PostingDto>,
    @Json(name = "myRunning") val myRunning: List<kr.devkyu.data.dto.PostingDto>
)