package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.Posting
import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class GetOtherUserResponse(
    @SerializedName("result") val result: OtherUser
) : BaseResponse()

data class OtherUser(
    @SerializedName("userInfo") val userInfo: OtherUserInfo,
    @SerializedName("userLogInfo") val userLogInfo: List<RunningLog>,
    @SerializedName("postTotalCount") val postTotalCount: Int,
    @SerializedName("userRunning") val userPosting: List<Posting>,
)

data class OtherUserInfo(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("age") val age: String,
    @SerializedName("diligence") val diligence: String,
    @SerializedName("job") val job: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("pace") val pace: String,
)