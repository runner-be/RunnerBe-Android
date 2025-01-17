package com.applemango.runnerbe.data.di

import com.applemango.runnerbe.data.mapper.AlarmMapper
import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.data.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.data.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.data.mapper.MyPageMapper
import com.applemango.runnerbe.data.mapper.NewUserMapper
import com.applemango.runnerbe.data.mapper.OtherUserMapper
import com.applemango.runnerbe.data.mapper.PostingDetailMapper
import com.applemango.runnerbe.data.mapper.PostingMapper
import com.applemango.runnerbe.data.mapper.ProfileUrlMapper
import com.applemango.runnerbe.data.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.data.mapper.RunningTalkMessageMapper
import com.applemango.runnerbe.data.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.data.mapper.SocialLoginMapper
import com.applemango.runnerbe.data.mapper.UserMapper
import com.applemango.runnerbe.data.mapperImpl.AlarmMapperImpl
import com.applemango.runnerbe.data.mapperImpl.CommonMapperImpl
import com.applemango.runnerbe.data.mapperImpl.JoinedRunnerMapperImpl
import com.applemango.runnerbe.data.mapperImpl.MonthlyRunningLogMapperImpl
import com.applemango.runnerbe.data.mapperImpl.MyPageMapperImpl
import com.applemango.runnerbe.data.mapperImpl.NewUserMapperImpl
import com.applemango.runnerbe.data.mapperImpl.OtherUserMapperImpl
import com.applemango.runnerbe.data.mapperImpl.PostingDetailMapperImpl
import com.applemango.runnerbe.data.mapperImpl.PostingMapperImpl
import com.applemango.runnerbe.data.mapperImpl.ProfileUrlMapperImpl
import com.applemango.runnerbe.data.mapperImpl.RunningLogDetailMapperImpl
import com.applemango.runnerbe.data.mapperImpl.RunningTalkMessageMapperImpl
import com.applemango.runnerbe.data.mapperImpl.RunningTalkRoomMapperImpl
import com.applemango.runnerbe.data.mapperImpl.SocialLoginMapperImpl
import com.applemango.runnerbe.data.mapperImpl.UserMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {

    @Binds
    @Singleton
    fun bindCommonMapper(
        commonMapperImpl: CommonMapperImpl
    ): CommonMapper

    @Binds
    @Singleton
    fun bindUserMapper(
        userMapperImpl: UserMapperImpl
    ): UserMapper

    @Binds
    @Singleton
    fun bindMyPageMapper(
        myPageMapperImpl: MyPageMapperImpl
    ): MyPageMapper

    @Binds
    @Singleton
    fun bindProfileUrlMapper(
        profileUrlMapperImpl: ProfileUrlMapperImpl
    ): ProfileUrlMapper

    @Binds
    @Singleton
    fun bindPostingMapper(
        postingMapperImpl: PostingMapperImpl
    ): PostingMapper

    @Binds
    @Singleton
    fun bindPostingDetailMapper(
        postingDetailMapperImpl: PostingDetailMapperImpl
    ): PostingDetailMapper

    @Binds
    @Singleton
    fun bindJoinedRunnerMapper(
        joinedRunnerMapperImpl: JoinedRunnerMapperImpl
    ): JoinedRunnerMapper

    @Binds
    @Singleton
    fun bindMonthlyRunningLogMapper(
        monthlyRunningLogMapperImpl: MonthlyRunningLogMapperImpl
    ): MonthlyRunningLogMapper

    @Binds
    @Singleton
    fun bindRunningLogDetailMapper(
        runningLogDetailMapperImpl: RunningLogDetailMapperImpl
    ): RunningLogDetailMapper

    @Binds
    @Singleton
    fun bindAlarmMapper(
        alarmMapperImpl: AlarmMapperImpl
    ): AlarmMapper

    @Binds
    @Singleton
    fun bindSocialLoginMapper(
        socialLoginMapperImpl: SocialLoginMapperImpl
    ): SocialLoginMapper

    @Binds
    @Singleton
    fun bindNewUserMapper(
        newUserMapperImpl: NewUserMapperImpl
    ): NewUserMapper

    @Binds
    @Singleton
    fun bindOtherUserMapper(
        otherUserMapperImpl: OtherUserMapperImpl
    ): OtherUserMapper

    @Binds
    @Singleton
    fun bindRunningTalkRoomMapper(
        runningTalkRoomMapperImpl: RunningTalkRoomMapperImpl
    ): RunningTalkRoomMapper


    @Binds
    @Singleton
    fun bindRunningTalkMessageMapper(
        runningTalkMessageMapperImpl: RunningTalkMessageMapperImpl
    ): RunningTalkMessageMapper
}