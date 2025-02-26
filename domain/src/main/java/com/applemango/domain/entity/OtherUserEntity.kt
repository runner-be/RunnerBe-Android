package com.applemango.domain.entity

data class OtherUserEntity(
    val otherUser: OtherUser
)

data class OtherUser(
    val userInfo: OtherUserInfo,
    val userLogInfo: List<RunningLog>,
    val postTotalCount: Int,
    val userPosting: List<PostingEntity>,
)

data class OtherUserInfo(
    val userId: Int,
    val nickName: String,
    val gender: String,
    val age: String,
    val diligence: String,
    val job: String,
    val profileImageUrl: String?,
    val pace: String,
)