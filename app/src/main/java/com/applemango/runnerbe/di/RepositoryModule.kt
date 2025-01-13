package com.applemango.runnerbe.di

import com.applemango.runnerbe.data.repositoryimpl.PostingRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningLogRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningTalkRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.UserRepositoryImpl
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

    @Singleton
    @Binds
    fun bindUserRepo(
        repo : UserRepositoryImpl
    ) : UserRepository

    @Singleton
    @Binds
    fun bindRunningTalkRepo(
        repo : RunningTalkRepositoryImpl
    ) : RunningTalkRepository

    @Singleton
    @Binds
    fun bindPostRepo(
        repository: PostingRepositoryImpl
    ) : PostingRepository

    @Singleton
    @Binds
    fun bindRunningLogRepo(
        repository: RunningLogRepositoryImpl
    ) : RunningLogRepository
}