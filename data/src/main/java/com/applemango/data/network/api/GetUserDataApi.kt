package com.applemango.data.network.api

import com.applemango.data.dto.MyPageDto
import retrofit2.Response
import retrofit2.http.*

interface GetUserDataApi {

    @GET("users/{userId}/myPage/v2")
    suspend fun getUserData(
        @Path("userId") userId: Int
    ): Response<MyPageDto>
}