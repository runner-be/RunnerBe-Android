package com.applemango.presentation.di

import com.applemango.presentation.ui.mapper.AddressMapper
import com.applemango.presentation.ui.mapper.AlarmMapper
import com.applemango.presentation.ui.mapper.JoinedRunnerMapper
import com.applemango.presentation.ui.mapper.MonthlyRunningLogMapper
import com.applemango.presentation.ui.mapper.OtherUserMyPageMapper
import com.applemango.presentation.ui.mapper.PostingDetailMapper
import com.applemango.presentation.ui.mapper.PostingMapper
import com.applemango.presentation.ui.mapper.RunningLogDetailMapper
import com.applemango.presentation.ui.mapper.RunningTalkMessageMapper
import com.applemango.presentation.ui.mapper.RunningTalkRoomMapper
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.mapperImpl.AddressMapperImpl
import com.applemango.presentation.ui.mapperImpl.AlarmMapperImpl
import com.applemango.presentation.ui.mapperImpl.JoinedRunnerMapperImpl
import com.applemango.presentation.ui.mapperImpl.MonthlyRunningLogMapperImpl
import com.applemango.presentation.ui.mapperImpl.OtherUserMyPageMapperImpl
import com.applemango.presentation.ui.mapperImpl.PostingDetailMapperImpl
import com.applemango.presentation.ui.mapperImpl.PostingMapperImpl
import com.applemango.presentation.ui.mapperImpl.RunningLogDetailMapperImpl
import com.applemango.presentation.ui.mapperImpl.RunningTalkMessageMapperImpl
import com.applemango.presentation.ui.mapperImpl.RunningTalkRoomMapperImpl
import com.applemango.presentation.ui.mapperImpl.UserMapperImpl
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
    abstract fun bindJoinedRunnerMapper(
        joinedRunnerMapperImpl: JoinedRunnerMapperImpl
    ): JoinedRunnerMapper

    @Binds
    @Singleton
    abstract fun bindRunningLogDetailMapper(
        runningLogDetailMapperImpl: RunningLogDetailMapperImpl
    ): RunningLogDetailMapper

    @Binds
    @Singleton
    abstract fun bindMonthlyRunningLogMapper(
        monthlyRunningLogMapper: MonthlyRunningLogMapperImpl
    ): MonthlyRunningLogMapper

    @Binds
    @Singleton
    abstract fun bindUserMapper(
        userMapperImpl: UserMapperImpl
    ): UserMapper

    @Binds
    @Singleton
    abstract fun bindOtherUserMyPageMapper(
        otherUserMyPageMapperImpl: OtherUserMyPageMapperImpl
    ): OtherUserMyPageMapper

    @Binds
    @Singleton
    abstract fun bindAddressMapper(
        addressMapperImpl: AddressMapperImpl
    ): AddressMapper

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
    abstract fun bindAlarmMapper(
        alarmMapper: AlarmMapperImpl
    ): AlarmMapper

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