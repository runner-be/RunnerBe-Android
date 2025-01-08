package kr.devkyu.domain.entity

data class OtherUserEntity(
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