package kr.devkyu.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class KakaoAddressDto(
    @Json(name = "documents") val documents: List<Document>,
    @Json(name = "meta") val meta: Meta
)

@JsonClass(generateAdapter = true)
data class Document(
    @Json(name = "address_name") val addressName: String,
    @Json(name = "category_group_code") val categoryGroupCode: String,
    @Json(name = "category_group_name") val categoryGroupName: String,
    @Json(name = "category_name") val categoryName: String,
    @Json(name = "distance") val distance: String?,
    @Json(name = "id") val id: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "place_name") val placeName: String,
    @Json(name = "place_url") val placeUrl: String,
    @Json(name = "road_address_name") val roadAddressName: String,
    @Json(name = "x") val longitude: String,
    @Json(name = "y") val latitude: String
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "is_end") val isEnd: Boolean,
    @Json(name = "pageable_count") val pageableCount: Int,
    @Json(name = "total_count") val totalCount: Int
)