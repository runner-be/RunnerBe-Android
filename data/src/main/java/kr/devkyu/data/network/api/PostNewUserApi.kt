package kr.devkyu.data.network.api

import kr.devkyu.data.dto.NewUserDto
import kr.devkyu.data.network.request.JoinUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostNewUserApi {
    @POST("/v2/users")
    suspend fun register(
        @Body body: JoinUserRequest
    ) : Response<NewUserDto>
}