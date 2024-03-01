package com.faddy.wgtunlib.module

import com.faddy.wgtunlib.data.AppDatabase
import com.faddy.wgtunlib.data.SettingsDao
import com.faddy.wgtunlib.data.TunnelConfigDao
import com.faddy.wgtunlib.data.repository.SettingsRepository
import com.faddy.wgtunlib.data.repository.SettingsRepositoryImpl
import com.faddy.wgtunlib.data.repository.TunnelConfigRepository
import com.faddy.wgtunlib.data.repository.TunnelConfigRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideSettingsDoa(appDatabase: AppDatabase): SettingsDao {
        return appDatabase.settingDao()
    }

    @Singleton
    @Provides
    fun provideTunnelConfigDoa(appDatabase: AppDatabase): TunnelConfigDao {
        return appDatabase.tunnelConfigDoa()
    }

    @Singleton
    @Provides
    fun provideTunnelConfigRepository(tunnelConfigDao: TunnelConfigDao): TunnelConfigRepository {
        return TunnelConfigRepositoryImpl(tunnelConfigDao)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(settingsDao: SettingsDao): SettingsRepository {
        return SettingsRepositoryImpl(settingsDao)
    }

}
