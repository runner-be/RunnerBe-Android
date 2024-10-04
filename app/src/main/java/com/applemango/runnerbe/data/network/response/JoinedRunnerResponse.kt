package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName

data class JoinedRunnerResponse(
    @SerializedName("userId")val userId: Int,
    @SerializedName("nickname")val nickname: String,
    @SerializedName("profileImageUrl")val profileImageUrl: String,
    @SerializedName("stampCode")val stampCode: String,
    @SerializedName("isOpened")val isOpened: Int
) : BaseResponse()
