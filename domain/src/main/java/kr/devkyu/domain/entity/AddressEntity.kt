package kr.devkyu.domain.entity

data class AddressEntity(
    val placeName: String,
    val roadAddress: String,
    val latitude: String,
    val longitude: String,
    val pageNumber: Int,
)
