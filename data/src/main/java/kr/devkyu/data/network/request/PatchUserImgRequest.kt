package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class PatchUserImgRequest(
    @Json(name = "profileImageUrl") val profileImageUrl: String?
)