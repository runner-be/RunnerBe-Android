package com.applemango.data.network.request

import androidx.annotation.Keep

@Keep
data class FirebaseTokenUpdateRequest(
    val deviceToken: String
)
