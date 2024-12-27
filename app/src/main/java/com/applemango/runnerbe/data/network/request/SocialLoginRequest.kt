package com.applemango.runnerbe.data.network.request

import com.squareup.moshi.Json

data class SocialLoginRequest(
    @Json(name = "accessToken") val accessToken: String
)