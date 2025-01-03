package com.applemango.runnerbe.domain.repository

import com.applemango.runnerbe.data.network.api.PostKakaoLoginAPI
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.data.network.response.SocialLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class KakaoLoginRepository @Inject constructor(private val service: PostKakaoLoginAPI) {
    fun getData(body: SocialLoginRequest): Flow<SocialLoginResponse> = flow {
        emit(service.kakaoLogin(body))
    }.flowOn(Dispatchers.IO)
}

