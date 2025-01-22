package kr.devkyu.data.network.api

import kr.devkyu.data.dto.RunningTalkRoomsDto
import retrofit2.Response
import retrofit2.http.GET

interface GetRunningTalkRoomsApi {

    @GET("messages")
    suspend fun getRunningTalkRooms(): Response<RunningTalkRoomsDto>
}