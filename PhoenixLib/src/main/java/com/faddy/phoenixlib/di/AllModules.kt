package com.faddy.phoenixlib.di

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.faddy.phoenixlib.SdkInternal
import com.faddy.phoenixlib.utils.SessionManagerInternal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllModules {

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManagerInternal(
        context.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
    )

    @Provides
    @Singleton
    fun providesInternalValidator() = SdkInternal().systemSetup()


    @Singleton
    @Provides
    fun providesContext(@ApplicationContext context: Context) = context
    @Singleton
    @Provides
    fun providesLocalBroadcastManager(@ApplicationContext context: Context) =
        LocalBroadcastManager.getInstance(context)

}