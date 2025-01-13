package com.applemango.runnerbe.entity

data class RunningTalkMessagesEntity(
    val roomInfo : List<RoomInfo>,
    val messages : List<RunningTalkMessage>
)

data class RoomInfo(
    val talkTitle : String,
    val pace: String
)

data class RunningTalkMessage(
    val messageId :Int,
    val content : String?,
    val imageUrl: String?,
    val createAt : String,
    val userId : Int,
    val nickName : String,
    val profileImageUrl : String?,
    val from: String, // Me or Others
    val whetherPostUser : String, // Y or N
    var isChecked: Boolean = false
)