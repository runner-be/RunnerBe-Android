package com.applemango.domain.entity

data class RunningTalkMessageEntity(
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
