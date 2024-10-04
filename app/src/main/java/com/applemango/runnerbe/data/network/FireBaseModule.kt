package com.applemango.runnerbe.data.network

import com.applemango.runnerbe.BuildConfig
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.api.FirebaseTokenUpdateApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FireBaseModule {

    val api : FirebaseTokenUpdateApi by lazy { apiInit() }
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

    private fun apiInit() : FirebaseTokenUpdateApi {
        val testRetrofit = firebaseRetrofit
        val retrofit = testRetrofit ?: run {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { firebaseRetrofit = it }
        }
        return retrofit.create(FirebaseTokenUpdateApi::class.java)
    }
}