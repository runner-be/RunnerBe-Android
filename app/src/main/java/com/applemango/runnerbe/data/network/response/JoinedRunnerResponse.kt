package com.applemango.runnerbe.data.network.response

import com.google.gson.annotations.SerializedName

data class JoinedRunnerResponse(
    val result: List<JoinedRunnerResult>
) : BaseResponse()

data class JoinedRunnerResult (
    @SerializedName("userId")val userId: Int,
    @SerializedName("nickname")val nickname: String,
    @SerializedName("profileImageUrl")val profileImageUrl: String?,
    @SerializedName("logId")val logId: String?,
    @SerializedName("isOpened")val isOpened: Int?,
    @SerializedName("stampCode")var stampCode: String?,
    @SerializedName("isCaptain")val isCaptain: Int,
)
