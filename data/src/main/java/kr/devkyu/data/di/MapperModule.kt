package kr.devkyu.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.devkyu.data.mapper.AlarmMapper
import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.JoinedRunnerMapper
import kr.devkyu.data.mapper.MonthlyRunningLogMapper
import kr.devkyu.data.mapper.MyPageMapper
import kr.devkyu.data.mapper.NewUserMapper
import kr.devkyu.data.mapper.OtherUserMapper
import kr.devkyu.data.mapper.PostingDetailMapper
import kr.devkyu.data.mapper.PostingMapper
import kr.devkyu.data.mapper.ProfileUrlMapper
import kr.devkyu.data.mapper.RunningLogDetailMapper
import kr.devkyu.data.mapper.RunningTalkMessageMapper
import kr.devkyu.data.mapper.RunningTalkRoomMapper
import kr.devkyu.data.mapper.SocialLoginMapper
import kr.devkyu.data.mapper.UserMapper
import kr.devkyu.data.mapperImpl.AlarmMapperImpl
import kr.devkyu.data.mapperImpl.CommonMapperImpl
import kr.devkyu.data.mapperImpl.JoinedRunnerMapperImpl
import kr.devkyu.data.mapperImpl.MonthlyRunningLogMapperImpl
import kr.devkyu.data.mapperImpl.MyPageMapperImpl
import kr.devkyu.data.mapperImpl.NewUserMapperImpl
import kr.devkyu.data.mapperImpl.OtherUserMapperImpl
import kr.devkyu.data.mapperImpl.PostingDetailMapperImpl
import kr.devkyu.data.mapperImpl.PostingMapperImpl
import kr.devkyu.data.mapperImpl.ProfileUrlMapperImpl
import kr.devkyu.data.mapperImpl.RunningLogDetailMapperImpl
import kr.devkyu.data.mapperImpl.RunningTalkMessageMapperImpl
import kr.devkyu.data.mapperImpl.RunningTalkRoomMapperImpl
import kr.devkyu.data.mapperImpl.SocialLoginMapperImpl
import kr.devkyu.data.mapperImpl.UserMapperImpl
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