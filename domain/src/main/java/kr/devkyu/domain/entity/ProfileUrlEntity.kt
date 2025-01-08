package kr.devkyu.domain.entity

data class ProfileUrlEntity(
    val userId: Int,
    val profileImageUrl: String?
): BaseEntity()
