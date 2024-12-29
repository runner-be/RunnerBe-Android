package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class PatchUserPaceRegisterRequest(
    @Json(name = "pace") val pace: String
)
