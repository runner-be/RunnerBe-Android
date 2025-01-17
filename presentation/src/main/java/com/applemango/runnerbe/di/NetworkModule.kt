package com.applemango.runnerbe.di

import com.applemango.runnerbe.BuildConfig
import com.applemango.runnerbe.data.network.BearerInterceptor
import com.applemango.runnerbe.data.network.XAccessTokenInterceptor
import com.applemango.runnerbe.data.network.ZonedDateTimeAdapter
import com.applemango.runnerbe.data.network.api.*
import com.applemango.runnerbe.data.network.api.DeleteRunningLogApi
import com.applemango.runnerbe.data.network.api.GetJoinedRunnersApi
import com.applemango.runnerbe.data.network.api.GetMonthlyRunningLogsApi
import com.applemango.runnerbe.data.network.api.GetRunningLogDetailApi
import com.applemango.runnerbe.data.network.api.PatchRunningLogApi
import com.applemango.runnerbe.data.network.api.PostRunningLogApi
import com.applemango.runnerbe.data.network.api.PostStampToJoinedRunnerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideOkHttpClient() : OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(BearerInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideZonedDateTimeAdapter(): ZonedDateTimeAdapter {
        return ZonedDateTimeAdapter()
    }

    @Singleton
    @Provides
    fun provideMoshi(
        zonedDateTimeAdapter: ZonedDateTimeAdapter
    ): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(zonedDateTimeAdapter)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideKakaoLoginApi(retrofit: Retrofit): PostKakaoLoginAPI =
        retrofit.create(PostKakaoLoginAPI::class.java)

    @Provides
    @Singleton
    fun provideNaverLoginApi(retrofit: Retrofit): PostNaverLoginAPI =
        retrofit.create(PostNaverLoginAPI::class.java)

    @Provides
    @Singleton
    fun provideUserDataApi(retrofit: Retrofit): GetUserDataApi =
        retrofit.create(GetUserDataApi::class.java)

    @Provides
    @Singleton
    fun provideRunningTalkApi(retrofit: Retrofit) : GetRunningTalkRoomsApi =
        retrofit.create(GetRunningTalkRoomsApi::class.java)

    @Provides
    @Singleton
    fun provideWithdrawalApi(retrofit: Retrofit) : DeleteUserApi =
        retrofit.create(DeleteUserApi::class.java)

    @Provides
    @Singleton
    fun providePatchAlarmApi(retrofit: Retrofit) : PatchAlarmApi =
        retrofit.create(PatchAlarmApi::class.java)

    @Provides
    @Singleton
    fun provideNicknameChangeApi(retrofit: Retrofit) : UpdateNicknameApi =
        retrofit.create(UpdateNicknameApi::class.java)

    @Provides
    @Singleton
    fun provideJobChangeApi(retrofit: Retrofit) : UpdateJobApi =
        retrofit.create(UpdateJobApi::class.java)

    @Provides
    @Singleton
    fun provideUserProfileImgChangeApi(retrofit: Retrofit) : PatchUserImageApi =
        retrofit.create(PatchUserImageApi::class.java)

    @Provides
    @Singleton
    fun provideGetBookmarkApi(retrofit: Retrofit) : GetBookmarksApi =
        retrofit.create(GetBookmarksApi::class.java)

    @Provides
    @Singleton
    fun provideWriteRunningApi(retrofit: Retrofit) : PostRunningApi =
        retrofit.create(PostRunningApi::class.java)

    @Provides
    @Singleton
    fun provideGetRunningListApi(retrofit: Retrofit) : GetRunningListApi =
        retrofit.create(GetRunningListApi::class.java)

    @Provides
    @Singleton
    fun provideAttendanceAccessionApi(retrofit: Retrofit) : PatchJoinedRunnerAttendanceApi =
        retrofit.create(PatchJoinedRunnerAttendanceApi::class.java)

    @Provides
    @Singleton
    fun provideRegisterUserApi(retrofit: Retrofit) : PostNewUserApi =
        retrofit.create(PostNewUserApi::class.java)

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
    fun providePostApply(retrofit: Retrofit) : PostApplyToPostApi =
        retrofit.create(PostApplyToPostApi::class.java)

    @Provides
    @Singleton
    fun provideWaitingRunnerAccept(retrofit: Retrofit) : PatchAppliedRunnerApi =
        retrofit.create(PatchAppliedRunnerApi::class.java)

    @Provides
    @Singleton
    fun provideBookmarkStatusChange(retrofit: Retrofit) : PostBookmarkedPostApi =
        retrofit.create(PostBookmarkedPostApi::class.java)

    @Provides
    @Singleton
    fun provideRunningTalkDetail(retrofit: Retrofit) : GetRunningTalkMessagesApi =
        retrofit.create(GetRunningTalkMessagesApi::class.java)

    @Provides
    @Singleton
    fun provideMessageSend(retrofit: Retrofit) : PostMessageApi =
        retrofit.create(PostMessageApi::class.java)

    @Provides
    @Singleton
    fun provideMessageReport(retrofit: Retrofit): PostMessageReportApi =
        retrofit.create(PostMessageReportApi::class.java)

    @Provides
    @Singleton
    fun provideDropPost(retrofit: Retrofit): DeletePostApi =
        retrofit.create(DeletePostApi::class.java)

    @Provides
    @Singleton
    fun provideReportPosting(retrofit: Retrofit): PostReportPostingApi =
        retrofit.create(PostReportPostingApi::class.java)

    @Provides
    @Singleton
    fun provideFirebaseTokenUpdate(retrofit: Retrofit): UpdateFirebaseTokenApi =
        retrofit.create(UpdateFirebaseTokenApi::class.java)

    @Provides
    @Singleton
    fun provideUserPaceRegist(retrofit: Retrofit): PatchUserPaceRegistApi =
        retrofit.create(PatchUserPaceRegistApi::class.java)

    @Provides
    @Singleton
    fun provideGetAddressList(retrofit: Retrofit): GetAddressResultListApi =
        retrofit.create(GetAddressResultListApi::class.java)

    @Provides
    @Singleton
    fun provideDeleteRunningLog(retrofit: Retrofit): DeleteRunningLogApi =
        retrofit.create(DeleteRunningLogApi::class.java)

    @Provides
    @Singleton
    fun provideGetJoinedRunnerList(retrofit: Retrofit): GetJoinedRunnersApi =
        retrofit.create(GetJoinedRunnersApi::class.java)

    @Provides
    @Singleton
    fun provideGetMonthlyRunningLogList(retrofit: Retrofit): GetMonthlyRunningLogsApi =
        retrofit.create(GetMonthlyRunningLogsApi::class.java)

    @Provides
    @Singleton
    fun provideGetRunningLogDetail(retrofit: Retrofit): GetRunningLogDetailApi =
        retrofit.create(GetRunningLogDetailApi::class.java)

    @Provides
    @Singleton
    fun providePatchRunningLog(retrofit: Retrofit): PatchRunningLogApi =
        retrofit.create(PatchRunningLogApi::class.java)

    @Provides
    @Singleton
    fun providePostRunningLog(retrofit: Retrofit): PostRunningLogApi =
        retrofit.create(PostRunningLogApi::class.java)

    @Provides
    @Singleton
    fun providePostStampToJoinedRunner(retrofit: Retrofit): PostStampToJoinedRunnerApi =
        retrofit.create(PostStampToJoinedRunnerApi::class.java)

    @Provides
    @Singleton
    fun provideGetOtherUserProfile(retrofit: Retrofit): GetOtherUserProfileApi =
        retrofit.create(GetOtherUserProfileApi::class.java)

    @Provides
    @Singleton
    fun provideGetNotifications(retrofit: Retrofit): GetAlarmsApi =
        retrofit.create(GetAlarmsApi::class.java)
}