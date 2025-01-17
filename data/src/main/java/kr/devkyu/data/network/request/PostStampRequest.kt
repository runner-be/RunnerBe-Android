package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class PostStampRequest(
    @Json(name = "targetId") val targetUserId: Int,
    @Json(name = "stampCode") val stampCode: String,
)
