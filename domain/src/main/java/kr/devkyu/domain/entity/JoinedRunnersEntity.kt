package kr.devkyu.domain.entity

data class JoinedRunnersEntity(
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String?,
    val logId: String?,
    val isOpened: Int?,
    var stampCode: String?,
    val isCaptain: Int,
): BaseEntity()
