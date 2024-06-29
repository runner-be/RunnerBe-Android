package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.GetRunningListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRunningListApi {
    @GET("users/main/v2/")
    suspend fun getRunningList(
        @Query("runningTag") runningTag: String,
        @Query("whetherEnd") whetherEnd: String,
        @Query("filter") priorityFilter : String,
        @Query("distanceFilter") distanceFilter : String,
        @Query("genderFilter") gender : String,
        @Query("ageFilterMax") maxAge : String,
        @Query("ageFilterMin") minAge : String,
        @Query("jobFilter") jobFilter : String,
        @Query("userLongitude") userLng : Double,
        @Query("userLatitude") userLat : Double,
        @Query("afterPartyFilter") afterPartyFilter: String = "A",
        @Query("keywordSearch") keyword : String = "N",
        @Query("userId") userId : Int? = null,
        @Query("pageSize") pageSize : Int = 10,
        @Query("page") page: Int = 1
    ) : Response<GetRunningListResponse>
}