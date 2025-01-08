package kr.devkyu.domain.entity

data class RegisterEntity(
    val insertedUserId: Int,
    val token: String
): BaseEntity()
