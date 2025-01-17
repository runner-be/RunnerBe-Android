package kr.devkyu.data.network

import kr.devkyu.data.network.api.UpdateFirebaseTokenApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FireBaseModule {

    val api : UpdateFirebaseTokenApi by lazy { apiInit() }
    private var firebaseRetrofit : Retrofit? = null

    private val httpClientBuilder = OkHttpClient().newBuilder().apply {
        this.addInterceptor {
            val token = RunnerBeApplication.mTokenPreference.getToken()
            val builder = it.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
            val request = builder.build()

            return@addInterceptor it.proceed(request)
        }
    }

    private fun apiInit() : UpdateFirebaseTokenApi {
        val testRetrofit = firebaseRetrofit
        val retrofit = testRetrofit ?: run {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .also { firebaseRetrofit = it }
        }
        return retrofit.create(UpdateFirebaseTokenApi::class.java)
    }
}