package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class PatchUserPaceRegisterRequest(
    @Json(name = "pace") val pace: String
)
