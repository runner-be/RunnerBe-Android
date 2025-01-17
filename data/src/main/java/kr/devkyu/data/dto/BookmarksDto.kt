package kr.devkyu.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookmarksDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : kr.devkyu.data.dto.BookmarkList
)

@JsonClass(generateAdapter = true)
data class BookmarkList(
    @Json(name = "bookMarkList") var bookMarkList: List<kr.devkyu.data.dto.PostingDto>?
)