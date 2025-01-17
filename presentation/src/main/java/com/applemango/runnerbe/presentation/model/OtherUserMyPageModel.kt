package com.applemango.runnerbe.presentation.model

import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.RunningLog

data class OtherUserMyPageModel(
    val otherUser: OtherUser
)

data class OtherUser(
    val userInfo: OtherUserInfo,
    val userLogInfo: List<RunningLog>,
    val postTotalCount: Int,
    val userPosting: List<PostingModel>,
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