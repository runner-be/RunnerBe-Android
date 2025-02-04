package com.applemango.data.network.api

import com.applemango.data.dto.RunningTalkRoomsDto
import retrofit2.Response
import retrofit2.http.GET

interface GetRunningTalkRoomsApi {

    @GET("messages")
    suspend fun getRunningTalkRooms(): Response<RunningTalkRoomsDto>
}