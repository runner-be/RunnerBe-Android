package com.applemango.runnerbe.data.network.response

import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.squareup.moshi.Json

data class UserDataResponse(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : GetMyPageResult
)

data class GetMyPageResult(
    @Json(name = "myInfo") val userInfo: UserInfo?,
    @Json(name = "myPosting") val myPosting: List<Posting>,
    @Json(name = "myRunning") val myRunning: List<Posting>
)