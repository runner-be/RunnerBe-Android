package com.applemango.runnerbe.di

import com.applemango.runnerbe.BuildConfig
import com.applemango.runnerbe.data.network.BearerInterceptor
import com.applemango.runnerbe.data.network.XAccessTokenInterceptor
import com.applemango.runnerbe.data.network.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * author : 두루
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(BearerInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()
    } else {
        OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideKakaoLoginApi(retrofit: Retrofit): KakaoLoginAPI =
        retrofit.create(KakaoLoginAPI::class.java)

    @Provides
    @Singleton
    fun provideNaverLoginApi(retrofit: Retrofit): NaverLoginAPI =
        retrofit.create(NaverLoginAPI::class.java)

    @Provides
    @Singleton
    fun provideUserDataApi(retrofit: Retrofit): GetUserDataApi =
        retrofit.create(GetUserDataApi::class.java)

    @Provides
    @Singleton
    fun provideRunningTalkApi(retrofit: Retrofit) : GetRunningTalkMessagesApi =
        retrofit.create(GetRunningTalkMessagesApi::class.java)

    @Provides
    @Singleton
    fun provideWithdrawalApi(retrofit: Retrofit) : WithdrawalApi =
        retrofit.create(WithdrawalApi::class.java)

    @Provides
    @Singleton
    fun providePatchAlarmApi(retrofit: Retrofit) : PatchAlarmApi =
        retrofit.create(PatchAlarmApi::class.java)

    @Provides
    @Singleton
    fun provideNicknameChangeApi(retrofit: Retrofit) : NicknameChangeApi =
        retrofit.create(NicknameChangeApi::class.java)

    @Provides
    @Singleton
    fun provideJobChangeApi(retrofit: Retrofit) : EditJobApi =
        retrofit.create(EditJobApi::class.java)

    @Provides
    @Singleton
    fun provideUserProfileImgChangeApi(retrofit: Retrofit) : PatchUserImageApi =
        retrofit.create(PatchUserImageApi::class.java)

    @Provides
    @Singleton
    fun provideGetBookmarkApi(retrofit: Retrofit) : GetBookmarkApi =
        retrofit.create(GetBookmarkApi::class.java)

    @Provides
    @Singleton
    fun provideWriteRunningApi(retrofit: Retrofit) : WriteRunningApi =
        retrofit.create(WriteRunningApi::class.java)

    @Provides
    @Singleton
    fun provideGetRunningListApi(retrofit: Retrofit) : GetRunningListApi =
        retrofit.create(GetRunningListApi::class.java)

    @Provides
    @Singleton
    fun provideAttendanceAccessionApi(retrofit: Retrofit) : AttendanceAccessionApi =
        retrofit.create(AttendanceAccessionApi::class.java)

    @Provides
    @Singleton
    fun provideRegisterUserApi(retrofit: Retrofit) : RegisterUserApi =
        retrofit.create(RegisterUserApi::class.java)

    @Provides
    @Singleton
    fun providePostDetailApi(retrofit: Retrofit) : GetPostDetailApi =
        retrofit.create(GetPostDetailApi::class.java)

    @Provides
    @Singleton
    fun providePostClosing(retrofit: Retrofit) : PostClosingApi =
        retrofit.create(PostClosingApi::class.java)

    @Provides
    @Singleton
    fun providePostApply(retrofit: Retrofit) : PostApplyApi =
        retrofit.create(PostApplyApi::class.java)

    @Provides
    @Singleton
    fun provideWaitingRunnerAccept(retrofit: Retrofit) : WhetherAcceptHandlingApi =
        retrofit.create(WhetherAcceptHandlingApi::class.java)

    @Provides
    @Singleton
    fun provideBookmarkStatusChange(retrofit: Retrofit) : BookMarkStatusChangeApi =
        retrofit.create(BookMarkStatusChangeApi::class.java)

    @Provides
    @Singleton
    fun provideRunningTalkDetail(retrofit: Retrofit) : GetRunningTalkDetailApi =
        retrofit.create(GetRunningTalkDetailApi::class.java)

    @Provides
    @Singleton
    fun provideMessageSend(retrofit: Retrofit) : MessageSendApi =
        retrofit.create(MessageSendApi::class.java)

    @Provides
    @Singleton
    fun provideMessageReport(retrofit: Retrofit): MessageReportApi =
        retrofit.create(MessageReportApi::class.java)

    @Provides
    @Singleton
    fun provideDropPost(retrofit: Retrofit): DropPostApi =
        retrofit.create(DropPostApi::class.java)

    @Provides
    @Singleton
    fun provideReportPosting(retrofit: Retrofit): PostReportPostingApi =
        retrofit.create(PostReportPostingApi::class.java)

    @Provides
    @Singleton
    fun provideFirebaseTokenUpdate(retrofit: Retrofit): FirebaseTokenUpdateApi =
        retrofit.create(FirebaseTokenUpdateApi::class.java)

    @Provides
    @Singleton
    fun provideUserPaceRegist(retrofit: Retrofit): PatchUserPaceRegistApi =
        retrofit.create(PatchUserPaceRegistApi::class.java)
}