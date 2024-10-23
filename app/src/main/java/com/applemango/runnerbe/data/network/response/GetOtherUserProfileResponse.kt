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
    @SerializedName("userRunning") val userPosting: List<OtherUserPosting>,
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

data class OtherUserPosting(
    @SerializedName("postId") val postId: Int,
    @SerializedName("postingTime") val postingTime: ZonedDateTime,
    @SerializedName("postUserId") val postUserId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("gatheringTime") val gatheringTime: ZonedDateTime,
    @SerializedName("runningTag") val runningTag: String,
    @SerializedName("age") val age: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("whetherEnd") val whetherEnd: String,
    @SerializedName("pace") val pace: String,
    @SerializedName("afterParty") val afterParty: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("gatheringId") val gatheringId: Int?,
    @SerializedName("logId") val logId: Int?
)