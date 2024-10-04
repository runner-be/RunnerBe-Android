package com.applemango.runnerbe.data.dto

import com.google.gson.annotations.SerializedName

data class RoomDetail(
    @SerializedName("roomInfo") val roomInfo : ArrayList<RoomInfo>,
    @SerializedName("messageList") val messages : ArrayList<Messages>
)

data class RoomInfo(
    @SerializedName("title") val talkTitle : String,
    @SerializedName("pace") val pace: String
)

data class Messages(
    @SerializedName("messageId") val messageId :Int,
    @SerializedName("content") val content : String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("createdAt") val createAt : String,
    @SerializedName("userId") val userId : Int,
    @SerializedName("nickName") val nickName : String,
    @SerializedName("profileImageUrl") val profileImageUrl : String?,
    @SerializedName("messageFrom") val from: String, // Me or Others
    @SerializedName("whetherPostUser") val whetherPostUser : String, // Y or N
    var isChecked: Boolean = false
)