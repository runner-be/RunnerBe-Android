package com.applemango.runnerbe.entity

data class RunningTalkMessagesEntity(
    val roomInfo : ArrayList<RoomInfo>,
    val messages : ArrayList<RunningTalkMessage>
): BaseEntity()

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