package com.applemango.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookmarksDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : BookmarkList
)

@JsonClass(generateAdapter = true)
data class BookmarkList(
    @Json(name = "bookMarkList") var bookMarkList: List<PostingDto>?
)