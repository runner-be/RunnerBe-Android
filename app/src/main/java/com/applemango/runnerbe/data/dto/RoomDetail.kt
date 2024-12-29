package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

data class RoomDetail(
    @Json(name = "roomInfo") val roomInfo : ArrayList<RoomInfo>,
    @Json(name = "messageList") val messages : ArrayList<Messages>
)

data class RoomInfo(
    @Json(name = "title") val talkTitle : String,
    @Json(name = "pace") val pace: String
)

data class Messages(
    @Json(name = "messageId") val messageId :Int,
    @Json(name = "content") val content : String?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "createdAt") val createAt : String,
    @Json(name = "userId") val userId : Int,
    @Json(name = "nickName") val nickName : String,
    @Json(name = "profileImageUrl") val profileImageUrl : String?,
    @Json(name = "messageFrom") val from: String, // Me or Others
    @Json(name = "whetherPostUser") val whetherPostUser : String, // Y or N
    var isChecked: Boolean = false
)