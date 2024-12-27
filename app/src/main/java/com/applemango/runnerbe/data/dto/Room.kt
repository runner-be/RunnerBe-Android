package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

data class Room(
    @Json(name = "roomId") val roomId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "repUserName") val repUserName: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String,
    @Json(name = "recentMessage") val recentMessage: String
)