package com.applemango.runnerbe.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoLocalResponse(
    val meta: KakaoAddressMeta,
    val documents: List<KakaoAddressDocument>
)

@JsonClass(generateAdapter = true)
data class KakaoAddressMeta (
    @Json(name = "total_count") val totalCount : Int,
    @Json(name = "pageable_count") val pageableCount: Int,
    @Json(name = "is_end") val isEnd: Boolean,
    @Json(name = "same_name") val sameName: SameName
)

@JsonClass(generateAdapter = true)
data class KakaoAddressDocument (
    @Json(name = "id")val id: String,
    @Json(name = "place_name")val placeName: String,
    @Json(name = "category_name")val categoryName: String,
    @Json(name = "category_group_code")val categoryGroupCode: String,
    @Json(name = "category_group_name")val categoryGroupName: String,
    @Json(name = "phone")val phone: String,
    @Json(name = "address_name")val addressName: String,
    @Json(name = "road_address_name")val roadAddressName: String,
    @Json(name = "x")val longitude: String,
    @Json(name = "y")val latitude: String,
    @Json(name = "place_url")val placeUrl: String,
    @Json(name = "distance")val distance: String?
)

@JsonClass(generateAdapter = true)
data class SameName (
    @Json(name = "region") val region: List<String>,
    @Json(name = "keyword") val keyword: String,
    @Json(name = "selected_region") val selectedRegion: String
)