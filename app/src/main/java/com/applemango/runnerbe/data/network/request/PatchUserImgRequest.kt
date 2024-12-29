package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class PatchUserImgRequest(
    @Json(name = "profileImageUrl") val profileImageUrl: String?
)