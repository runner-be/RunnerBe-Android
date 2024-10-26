package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName

data class WriteRunningRequest(
    @SerializedName("title") val runningTitle: String,
    @SerializedName("gatheringTime") val gatheringTime : String,
    @SerializedName("runningTime") val runningTime: String,
    @SerializedName("gatherLatitude") val latitude: Double,
    @SerializedName("gatherLongitude") val longitude : Double,
    @SerializedName("placeName") val placeName : String,
    @SerializedName("placeAddress") val placeAddress : String,
    @SerializedName("placeExplain") val placeExplain : String,
    @SerializedName("runningTag") val runningTag : String,
    @SerializedName("ageMin") val minAge : Int,
    @SerializedName("ageMax") val maxAge : Int,
    @SerializedName("peopleNum") val numberOfRunner : Int,
    @SerializedName("contents") val contents : String?,
    @SerializedName("runnerGender") val gender : String,
    @SerializedName("paceGrade") val paceGrade: String,
    @SerializedName("afterParty") val isAfterParty: Int
)
