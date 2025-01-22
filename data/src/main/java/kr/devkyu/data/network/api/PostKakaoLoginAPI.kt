package kr.devkyu.data.network.api

import kr.devkyu.data.dto.SocialLoginDto
import kr.devkyu.data.network.request.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostKakaoLoginAPI {
    @POST("/users/kakao-login")
    suspend fun kakaoLogin(
        @Body body: SocialLoginRequest
    ) : Response<SocialLoginDto>
}