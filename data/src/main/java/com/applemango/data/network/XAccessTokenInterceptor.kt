package com.applemango.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * author : 두루
 */
class XAccessTokenInterceptor @Inject constructor(
    private val jwtToken: String
): Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request().newBuilder()
//        val refreshToken: String? =  tokenSPreference.getRefreshToken()

        jwtToken.let { token ->
            originalRequest.addHeader("MobileType","AOS")
            originalRequest.addHeader("x-access-token", token)
            //            if(refreshToken != null){
            //                builder.addHeader("Authorization", "Bearer "+jwtToken)
            //                builder.addHeader("refresh-token", refreshToken!!)
            //            }
        }

        return chain.proceed(originalRequest.build())
    }
}