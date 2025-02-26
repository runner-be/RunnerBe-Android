package com.applemango.data.di

import com.applemango.data.mapper.AlarmMapper
import com.applemango.data.mapper.CommonMapper
import com.applemango.data.mapper.JoinedRunnerMapper
import com.applemango.data.mapper.MonthlyRunningLogMapper
import com.applemango.data.mapper.MyPageMapper
import com.applemango.data.mapper.NewUserMapper
import com.applemango.data.mapper.OtherUserMapper
import com.applemango.data.mapper.PostingDetailMapper
import com.applemango.data.mapper.PostingMapper
import com.applemango.data.mapper.ProfileUrlMapper
import com.applemango.data.mapper.RunningLogDetailMapper
import com.applemango.data.mapper.RunningTalkMessageMapper
import com.applemango.data.mapper.RunningTalkRoomMapper
import com.applemango.data.mapper.SocialLoginMapper
import com.applemango.data.mapper.UserMapper
import com.applemango.data.mapperImpl.AlarmMapperImpl
import com.applemango.data.mapperImpl.CommonMapperImpl
import com.applemango.data.mapperImpl.JoinedRunnerMapperImpl
import com.applemango.data.mapperImpl.MonthlyRunningLogMapperImpl
import com.applemango.data.mapperImpl.MyPageMapperImpl
import com.applemango.data.mapperImpl.NewUserMapperImpl
import com.applemango.data.mapperImpl.OtherUserMapperImpl
import com.applemango.data.mapperImpl.PostingDetailMapperImpl
import com.applemango.data.mapperImpl.PostingMapperImpl
import com.applemango.data.mapperImpl.ProfileUrlMapperImpl
import com.applemango.data.mapperImpl.RunningLogDetailMapperImpl
import com.applemango.data.mapperImpl.RunningTalkMessageMapperImpl
import com.applemango.data.mapperImpl.RunningTalkRoomMapperImpl
import com.applemango.data.mapperImpl.SocialLoginMapperImpl
import com.applemango.data.mapperImpl.UserMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    @Singleton
    abstract fun bindCommonMapper(
        commonMapperImpl: CommonMapperImpl
    ): CommonMapper

    @Binds
    @Singleton
    abstract fun bindUserMapper(
        userMapperImpl: UserMapperImpl
    ): UserMapper

    @Binds
    @Singleton
    abstract fun bindMyPageMapper(
        myPageMapperImpl: MyPageMapperImpl
    ): MyPageMapper

    @Binds
    @Singleton
    abstract fun bindProfileUrlMapper(
        profileUrlMapperImpl: ProfileUrlMapperImpl
    ): ProfileUrlMapper

    @Binds
    @Singleton
    abstract fun bindPostingMapper(
        postingMapperImpl: PostingMapperImpl
    ): PostingMapper

    @Binds
    @Singleton
    abstract fun bindPostingDetailMapper(
        postingDetailMapperImpl: PostingDetailMapperImpl
    ): PostingDetailMapper

    @Binds
    @Singleton
    abstract fun bindJoinedRunnerMapper(
        joinedRunnerMapperImpl: JoinedRunnerMapperImpl
    ): JoinedRunnerMapper

    @Binds
    @Singleton
    abstract fun bindMonthlyRunningLogMapper(
        monthlyRunningLogMapperImpl: MonthlyRunningLogMapperImpl
    ): MonthlyRunningLogMapper

    @Binds
    @Singleton
    abstract fun bindRunningLogDetailMapper(
        runningLogDetailMapperImpl: RunningLogDetailMapperImpl
    ): RunningLogDetailMapper

    @Binds
    @Singleton
    abstract fun bindAlarmMapper(
        alarmMapperImpl: AlarmMapperImpl
    ): AlarmMapper

    @Binds
    @Singleton
    abstract fun bindSocialLoginMapper(
        socialLoginMapperImpl: SocialLoginMapperImpl
    ): SocialLoginMapper

    @Binds
    @Singleton
    abstract fun bindNewUserMapper(
        newUserMapperImpl: NewUserMapperImpl
    ): NewUserMapper

    @Binds
    @Singleton
    abstract fun bindOtherUserMapper(
        otherUserMapperImpl: OtherUserMapperImpl
    ): OtherUserMapper

    @Binds
    @Singleton
    abstract fun bindRunningTalkRoomMapper(
        runningTalkRoomMapperImpl: RunningTalkRoomMapperImpl
    ): RunningTalkRoomMapper

    @Binds
    @Singleton
    abstract fun bindRunningTalkMessageMapper(
        runningTalkMessageMapperImpl: RunningTalkMessageMapperImpl
    ): RunningTalkMessageMapper
}