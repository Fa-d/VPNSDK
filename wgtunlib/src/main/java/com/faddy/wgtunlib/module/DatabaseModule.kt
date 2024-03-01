package com.faddy.wgtunlib.module

import android.content.Context
import androidx.room.Room
import com.faddy.wgtunlib.R
import com.faddy.wgtunlib.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.db_name),
        ).fallbackToDestructiveMigration().build()
    }
}
