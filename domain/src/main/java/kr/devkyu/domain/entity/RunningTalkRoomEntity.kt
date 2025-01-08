package kr.devkyu.domain.entity

data class RunningTalkRoomsEntity(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String? = null,
    val result : List<RunningTalkRoom>
)

data class RunningTalkRoom(
    val roomId: Int,
    val title: String,
    val repUserName: String,
    val profileImageUrl: String,
    val recentMessage: String
): BaseEntity()