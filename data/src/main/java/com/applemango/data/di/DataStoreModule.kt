package com.applemango.data.di

import android.content.Context
import com.applemango.data.network.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore {
        return UserDataStore(context)
    }

    @Provides
    fun provideContext(
        @ApplicationContext context: Context
    ): Context = context
}