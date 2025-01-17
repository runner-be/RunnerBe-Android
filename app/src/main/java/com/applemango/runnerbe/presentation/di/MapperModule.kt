package com.applemango.runnerbe.presentation.di

import com.applemango.runnerbe.presentation.mapper.AddressMapper
import com.applemango.runnerbe.presentation.mapper.AlarmMapper
import com.applemango.runnerbe.presentation.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.presentation.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.presentation.mapper.OtherUserMyPageMapper
import com.applemango.runnerbe.presentation.mapper.PostingDetailMapper
import com.applemango.runnerbe.presentation.mapper.PostingMapper
import com.applemango.runnerbe.presentation.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.presentation.mapper.RunningTalkMessageMapper
import com.applemango.runnerbe.presentation.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.presentation.mapper.UserMapper
import com.applemango.runnerbe.presentation.mapperImpl.AddressMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.AlarmMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.JoinedRunnerMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.MonthlyRunningLogMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.OtherUserMyPageMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.PostingDetailMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.PostingMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.RunningLogDetailMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.RunningTalkMessageMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.RunningTalkRoomMapperImpl
import com.applemango.runnerbe.presentation.mapperImpl.UserMapperImpl
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
    fun bindJoinedRunnerMapper(
        joinedRunnerMapperImpl: JoinedRunnerMapperImpl
    ): JoinedRunnerMapper

    @Binds
    @Singleton
    fun bindRunningLogDetailMapper(
        runningLogDetailMapperImpl: RunningLogDetailMapperImpl
    ): RunningLogDetailMapper

    @Binds
    @Singleton
    fun bindMonthlyRunningLogMapper(
        monthlyRunningLogMapper: MonthlyRunningLogMapperImpl
    ): MonthlyRunningLogMapper

    @Binds
    @Singleton
    fun bindUserMapper(
        userMapperImpl: UserMapperImpl
    ): UserMapper

    @Binds
    @Singleton
    fun bindOtherUserMyPageMapper(
        otherUserMyPageMapperImpl: OtherUserMyPageMapperImpl
    ): OtherUserMyPageMapper

    @Binds
    @Singleton
    fun bindAddressMapper(
        addressMapperImpl: AddressMapperImpl
    ): AddressMapper

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
    fun bindAlarmMapper(
        alarmMapper: AlarmMapperImpl
    ): AlarmMapper

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