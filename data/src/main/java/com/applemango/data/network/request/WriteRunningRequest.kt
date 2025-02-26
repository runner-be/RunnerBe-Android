package com.applemango.data.network.request

import com.squareup.moshi.Json

data class WriteRunningRequest(
    @Json(name = "title") val runningTitle: String,
    @Json(name = "gatheringTime") val gatheringTime : String,
    @Json(name = "runningTime") val runningTime: String,
    @Json(name = "gatherLatitude") val latitude: Double,
    @Json(name = "gatherLongitude") val longitude : Double,
    @Json(name = "placeName") val placeName : String,
    @Json(name = "placeAddress") val placeAddress : String,
    @Json(name = "placeExplain") val placeExplain : String,
    @Json(name = "runningTag") val runningTag : String,
    @Json(name = "ageMin") val minAge : Int,
    @Json(name = "ageMax") val maxAge : Int,
    @Json(name = "peopleNum") val numberOfRunner : Int,
    @Json(name = "contents") val contents : String?,
    @Json(name = "runnerGender") val gender : String,
    @Json(name = "paceGrade") val paceGrade: String,
    @Json(name = "afterParty") val isAfterParty: Int
)
