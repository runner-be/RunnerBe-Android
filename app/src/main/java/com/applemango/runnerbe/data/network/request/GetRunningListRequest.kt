package com.applemango.runnerbe.data.network.request

data class GetRunningListRequest(
    val whetherEnd: String,
    val priorityFilter : String,
    val distanceFilter : String,
    val gender : String,
    val maxAge : String,
    val minAge : String,
    val jobFilter : String,
    val userLng : Double,
    val userLat : Double,
    val afterPartyFilter: String,
    val keyword : String = "N",
    val userId : Int?,
    val pageSize : Int,
    val page : Int
)
