package com.faddy.wgtunlib.data.repository

import com.faddy.wgtunlib.data.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun save(settings: Settings)

    fun getSettingsFlow(): Flow<Settings>

    suspend fun getSettings(): Settings

    suspend fun getAll(): List<Settings>
}
