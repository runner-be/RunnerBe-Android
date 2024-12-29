package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.Posting
import com.squareup.moshi.Json

data class GetOtherUserResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: OtherUser
)

data class OtherUser(
    @Json(name = "userInfo") val userInfo: OtherUserInfo,
    @Json(name = "userLogInfo") val userLogInfo: List<RunningLog>,
    @Json(name = "postTotalCount") val postTotalCount: Int,
    @Json(name = "userRunning") val userPosting: List<Posting>,
)

data class OtherUserInfo(
    @Json(name = "userId") val userId: Int,
    @Json(name = "nickName") val nickName: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "age") val age: String,
    @Json(name = "diligence") val diligence: String,
    @Json(name = "job") val job: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    @Json(name = "pace") val pace: String,
)