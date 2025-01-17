package com.applemango.runnerbe.data.di

import com.applemango.runnerbe.data.repositoryimpl.AlarmRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.JoinedRunnerRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.PostingRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningLogRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningTalkRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.UserRepositoryImpl
import com.applemango.runnerbe.repository.AlarmRepository
import com.applemango.runnerbe.repository.JoinedRunnerRepository
import com.applemango.runnerbe.repository.PostingRepository
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.repository.RunningTalkRepository
import com.applemango.runnerbe.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl
    ): AlarmRepository

    @Binds
    @Singleton
    fun bindJoinedRunnerRepository(
        joinedRunnerRepositoryImpl: JoinedRunnerRepositoryImpl
    ): JoinedRunnerRepository

    @Binds
    @Singleton
    fun bindPostingRepository(
        postingRepositoryImpl: PostingRepositoryImpl
    ): PostingRepository

    @Binds
    @Singleton
    fun bindRunningLogRepository(
        runningLogRepositoryImpl: RunningLogRepositoryImpl
    ): RunningLogRepository

    @Binds
    @Singleton
    fun bindRunningTalkRepository(
        runningTalkRepositoryImpl: RunningTalkRepositoryImpl
    ): RunningTalkRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}