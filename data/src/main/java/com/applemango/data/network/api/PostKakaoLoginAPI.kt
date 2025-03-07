package com.applemango.data.network.api

import com.applemango.data.dto.SocialLoginDto
import com.applemango.data.network.request.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostKakaoLoginAPI {
    @POST("/users/kakao-login")
    suspend fun kakaoLogin(
        @Body body: SocialLoginRequest
    ) : Response<SocialLoginDto>
}