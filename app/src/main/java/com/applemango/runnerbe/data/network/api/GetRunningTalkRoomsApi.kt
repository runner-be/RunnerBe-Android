package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.RunningTalksResponse
import retrofit2.Response
import retrofit2.http.GET

interface GetRunningTalkRoomsApi {

    @GET("messages")
    suspend fun getMessages(): Response<RunningTalksResponse>
}