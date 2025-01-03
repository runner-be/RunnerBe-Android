package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.JoinUserRequest
import com.applemango.runnerbe.data.network.response.JoinUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostNewUserApi {
    @POST("/v2/users")
    suspend fun register(@Body body: JoinUserRequest) : Response<JoinUserResponse>
}