package com.applemango.data.network.request

import com.squareup.moshi.Json

data class PatchUserImgRequest(
    @Json(name = "profileImageUrl") val profileImageUrl: String?
)