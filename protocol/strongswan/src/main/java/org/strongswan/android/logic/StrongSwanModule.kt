package org.strongswan.android.logic

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StrongSwanModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }

    @Singleton
    @Provides
    fun provideExecutorService() : ExecutorService {
        return Executors.newFixedThreadPool(4)
    }

    @Singleton
    @Provides
    fun provideHandler() : Handler {
        return HandlerCompat.createAsync(Looper.getMainLooper())
    }

}