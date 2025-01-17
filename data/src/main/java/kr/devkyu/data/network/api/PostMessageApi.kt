package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostMessageApi {

    @POST("messages/rooms/{roomId}")
    suspend fun sendMessage(@Path("roomId") roomId: Int, @Body request : SendMessageRequest): Response<kr.devkyu.data.dto.CommonDto>
}