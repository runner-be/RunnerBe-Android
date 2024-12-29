package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class EditNicknameRequest(
    @Json(name = "nickName") val nickName: String
)