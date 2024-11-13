package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("result") val result : GetMyPageResult
) : BaseResponse()

data class GetMyPageResult(
    @SerializedName("myInfo") val userInfo: UserInfo,
    @SerializedName("myPosting") val myPosting: List<Posting>,
    @SerializedName("myRunning") val myRunning: List<Posting>
)