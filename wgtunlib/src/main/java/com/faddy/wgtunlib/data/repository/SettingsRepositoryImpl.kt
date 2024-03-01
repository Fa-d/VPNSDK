package com.faddy.wgtunlib.data.repository

import com.faddy.wgtunlib.data.SettingsDao
import com.faddy.wgtunlib.data.model.Settings
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val settingsDoa: SettingsDao) : SettingsRepository {

    override suspend fun save(settings: Settings) {
        settingsDoa.save(settings)
    }

    override fun getSettingsFlow(): Flow<Settings> {
        return settingsDoa.getSettingsFlow()
    }

    override suspend fun getSettings(): Settings {
        return settingsDoa.getAll().firstOrNull() ?: Settings()
    }

    override suspend fun getAll(): List<Settings> {
        return settingsDoa.getAll()
    }
}
