package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

data class Message(
    @Json(name = "messageId") val messageId: Int,
    @Json(name = "content") val content: String,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "userId") val userId: Int,
    @Json(name = "nickName") val nickName: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String,
    // me 이면 내가 보낸것, other 이면 다른사람이 보낸것
    @Json(name = "messageFrom") val messageFrom: String,
    // Y이면 글쓴이, 아니먄 N
    @Json(name = "whetherPostUser") val whetherPostUser: String
) {
}
