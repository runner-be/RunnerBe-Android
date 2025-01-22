package kr.devkyu.data.network.api

import retrofit2.Response
import retrofit2.http.*

interface GetUserDataApi {

    @GET("users/{userId}/myPage/v2")
    suspend fun getUserData(
        @Path("userId") userId: Int
    ): Response<kr.devkyu.data.dto.MyPageDto>
}