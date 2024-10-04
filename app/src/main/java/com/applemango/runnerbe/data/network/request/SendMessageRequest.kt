package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("content") val content : String?,
    @SerializedName("imageUrl") val imageUrl: String?
)
