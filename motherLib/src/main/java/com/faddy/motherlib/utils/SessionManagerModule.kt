package com.faddy.motherlib.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionManagerModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            "user_info_mother_lib", Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideSessionManager(preferences: SharedPreferences) = SessionManager(preferences)
}