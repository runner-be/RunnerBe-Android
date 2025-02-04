package com.applemango.presentation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

   companion object {
       @Provides
       fun provideApplicationContext(
           @ApplicationContext context: Context
       ) : Context = context
   }
}