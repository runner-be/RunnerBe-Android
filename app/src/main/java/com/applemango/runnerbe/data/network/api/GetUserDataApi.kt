package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.new.UserDto
import retrofit2.Response
import retrofit2.http.*

interface GetUserDataApi {

    @GET("users/{userId}/myPage/v2")
    suspend fun getUserData(
        @Path("userId") userId: Int
    ): Response<UserDto>
}