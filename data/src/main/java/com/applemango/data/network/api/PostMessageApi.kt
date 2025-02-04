package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostMessageApi {

    @POST("messages/rooms/{roomId}")
    suspend fun sendMessage(
        @Path("roomId") roomId: Int,
        @Body request : SendMessageRequest
    ): Response<CommonDto>
}