package com.applemango.runnerbe.di

import com.applemango.runnerbe.data.repositoryimpl.PostRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningLogRepositoryImpl
import com.applemango.runnerbe.data.repositoryimpl.RunningTalkRepositoryImpl
import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.data.repositoryimpl.UserRepositoryImpl
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.domain.repository.RunningTalkRepository
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
        repository: PostRepositoryImpl
    ) : PostRepository

    @Singleton
    @Binds
    fun bindRunningLogRepo(
        repository: RunningLogRepositoryImpl
    ) : RunningLogRepository
}