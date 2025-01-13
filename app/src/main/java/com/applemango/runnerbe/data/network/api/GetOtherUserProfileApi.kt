package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.new.OtherUserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetOtherUserProfileApi {

    @GET("/users/{userId}/userPage/v2")
    suspend fun getOtherUserProfile(
        @Path("userId") userId: Int
    ): Response<OtherUserDto>
}