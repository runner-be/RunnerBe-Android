package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.SocialLoginDto
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostNaverLoginAPI {
    @POST("/users/naver-login")
    suspend fun naverLogin(
        @Body body: SocialLoginRequest
    ) : Response<SocialLoginDto>
}