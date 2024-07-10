package com.faddy.wgtunlib

import android.content.Context
import com.faddy.wgtunlib.service.CustomWgCore
import com.wireguard.android.backend.GoBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WgModule {
    @Provides
    @Singleton
    fun providesWGCore(backend: GoBackend) = CustomWgCore(backend)


    @Provides
    @Singleton
    fun providesGoBackend(@ApplicationContext context: Context) = GoBackend(context)
}