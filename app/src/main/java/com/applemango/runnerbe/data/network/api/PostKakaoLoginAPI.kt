package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.data.network.response.SocialLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PostKakaoLoginAPI {
    @POST("/users/kakao-login")
    suspend fun kakaoLogin(@Body body: SocialLoginRequest) : SocialLoginResponse
}