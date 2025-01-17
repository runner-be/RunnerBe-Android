package kr.devkyu.data.network

import com.applemango.runnerbe.RunnerBeApplication.Companion.mTokenPreference
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * author : 두루
 */
class XAccessTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwtToken: String? = mTokenPreference.getToken()

        // refresh token
        val refreshToken: String? =  mTokenPreference.getRefreshToken()

        if (jwtToken != null) {
            builder.addHeader("MobileType","AOS")
            builder.addHeader("x-access-token", jwtToken)
//            if(refreshToken != null){
////                builder.addHeader("Authorization", "Bearer "+jwtToken)
//                // refresh_token
////                builder.addHeader("refresh-token", refreshToken!!)
//            }
        }
        return chain.proceed(builder.build())
    }
}