package kr.devkyu.domain.entity

data class SocialLoginEntity(
    val uuid: String?,
    val userId: Int?,
    val jwt: String?,
    val responseMessage: String
): BaseEntity()
