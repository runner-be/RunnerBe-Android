package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.new.RunningTalkMessagesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetRunningTalkMessagesApi {

    @GET("/messages/rooms/{roomId}")
    suspend fun getRunningTalkMessages(@Path("roomId") roomId : Int):Response<RunningTalkMessagesDto>
}