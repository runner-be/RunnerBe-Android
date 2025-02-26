package com.applemango.data.network.request

import com.squareup.moshi.Json

data class EditNicknameRequest(
    @Json(name = "nickName") val nickName: String
)