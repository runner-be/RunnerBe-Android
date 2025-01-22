package kr.devkyu.data.di

import com.applemango.runnerbe.repository.AlarmRepository
import com.applemango.runnerbe.repository.FirebaseTokenRepository
import com.applemango.runnerbe.repository.JoinedRunnerRepository
import com.applemango.runnerbe.repository.PostingRepository
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.repository.RunningTalkRepository
import com.applemango.runnerbe.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.devkyu.data.repositoryimpl.AlarmRepositoryImpl
import kr.devkyu.data.repositoryimpl.FirebaseTokenRepositoryImpl
import kr.devkyu.data.repositoryimpl.JoinedRunnerRepositoryImpl
import kr.devkyu.data.repositoryimpl.PostingRepositoryImpl
import kr.devkyu.data.repositoryimpl.RunningLogRepositoryImpl
import kr.devkyu.data.repositoryimpl.RunningTalkRepositoryImpl
import kr.devkyu.data.repositoryimpl.UserRepositoryImpl
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

    @Binds
    @Singleton
    fun bindFirebaseRepository(
        firebaseTokenRepository: FirebaseTokenRepositoryImpl
    ): FirebaseTokenRepository
}