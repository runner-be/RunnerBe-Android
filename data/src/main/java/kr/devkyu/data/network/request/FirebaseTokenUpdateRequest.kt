package kr.devkyu.data.network.request

import com.google.errorprone.annotations.Keep

@Keep
data class FirebaseTokenUpdateRequest(
    val deviceToken: String
)
