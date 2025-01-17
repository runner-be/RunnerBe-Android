package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class WithdrawalUserRequest(
    @Json(name = "secret_key") val secretKey: String
)