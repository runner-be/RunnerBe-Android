package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.data.network.response.SocialLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PostNaverLoginAPI {
    @POST("/users/naver-login")
    suspend fun naverLogin(@Body body: SocialLoginRequest) : SocialLoginResponse
}