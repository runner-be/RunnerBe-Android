package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class EditJobRequest(
    @Json(name = "job") val job: String
)