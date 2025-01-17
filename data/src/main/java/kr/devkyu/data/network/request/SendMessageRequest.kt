package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class SendMessageRequest(
    @Json(name = "content") val content : String?,
    @Json(name = "imageUrl") val imageUrl: String?
)
