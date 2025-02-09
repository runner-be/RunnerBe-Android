package com.applemango.data.network

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * bearer 토큰 필요한 api 사용시 accessToken 유효한지 검사
 * 유효하지 않다면 재발급 api 호출
 * refreshToken 유효한 경우, 정상적으로 accessToken 재발급 후 기존 api 동작 완료
 * 사용시 주석 풀고 사용하기
 * author : 두루
 */

class BearerInterceptor @Inject constructor(
    private val userDataStore: UserDataStore
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val bearerResponse = chain.request()
        if (bearerResponse.method == "HTTP 403 ") {
//            var refreshToken = RunnerBeApplication.sSharedPreferences.getString("refresh-token", null)

//            val response = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(RefreshAPI::class.java)

//            val request = PostRefreshRequest(accessToken!!, refreshToken!!)
//            val result = response.postRefresh(request)
//
//            val editor = MyApplication.editor
//            editor.putString("X-ACCESS-TOKEN", result.blockingGet().accessToken)
//            editor.putString("refresh-token", result.blockingGet().refreshToken)
//            editor.commit()
//
//            accessToken = result.blockingGet().accessToken

            val jwtToken = runBlocking {
                userDataStore.getJwtToken()
            }
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $jwtToken")
                    .build()
//            val newRequest = chain.request().newBuilder().build()
            return chain.proceed(newRequest)
        } else {
            val response = chain.request()
            return chain.proceed(response)
        }
    }
}