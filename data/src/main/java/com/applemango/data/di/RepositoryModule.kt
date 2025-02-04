package com.applemango.data.di

import com.applemango.data.repositoryimpl.AlarmRepositoryImpl
import com.applemango.data.repositoryimpl.FirebaseTokenRepositoryImpl
import com.applemango.data.repositoryimpl.JoinedRunnerRepositoryImpl
import com.applemango.data.repositoryimpl.PostingRepositoryImpl
import com.applemango.data.repositoryimpl.RunningLogRepositoryImpl
import com.applemango.data.repositoryimpl.RunningTalkRepositoryImpl
import com.applemango.data.repositoryimpl.UserRepositoryImpl
import com.applemango.domain.repository.AlarmRepository
import com.applemango.domain.repository.FirebaseTokenRepository
import com.applemango.domain.repository.JoinedRunnerRepository
import com.applemango.domain.repository.PostingRepository
import com.applemango.domain.repository.RunningLogRepository
import com.applemango.domain.repository.RunningTalkRepository
import com.applemango.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl
    ): AlarmRepository

    @Binds
    @Singleton
    abstract fun bindJoinedRunnerRepository(
        joinedRunnerRepositoryImpl: JoinedRunnerRepositoryImpl
    ): JoinedRunnerRepository

    @Binds
    @Singleton
    abstract fun bindPostingRepository(
        postingRepositoryImpl: PostingRepositoryImpl
    ): PostingRepository

    @Binds
    @Singleton
    abstract fun bindRunningLogRepository(
        runningLogRepositoryImpl: RunningLogRepositoryImpl
    ): RunningLogRepository

    @Binds
    @Singleton
    abstract fun bindRunningTalkRepository(
        runningTalkRepositoryImpl: RunningTalkRepositoryImpl
    ): RunningTalkRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(
        firebaseTokenRepository: FirebaseTokenRepositoryImpl
    ): FirebaseTokenRepository
}