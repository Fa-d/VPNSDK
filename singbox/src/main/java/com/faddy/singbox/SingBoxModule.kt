package com.faddy.singbox

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingBoxModule {
    @Provides
    @Singleton
    fun providesSingBoxCore(
        @ApplicationContext context: Context, customApplication: CustomApplication
    ) = SingBoxCore(context, customApplication)

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesCustomApplication(@ApplicationContext context: Context) = CustomApplication(context)
}