package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName

data class PostStampRequest(
    @SerializedName("targetId") val targetUserId: Int,
    @SerializedName("stampCode") val stampCode: String,
)
