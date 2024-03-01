package com.faddy.singboxsdk.database

import com.faddy.singboxsdk.bg.ProxyService
import com.faddy.singboxsdk.bg.VPNService
import com.faddy.singboxsdk.constant.ServiceMode
import com.faddy.singboxsdk.constant.SettingsKey
import com.faddy.singboxsdk.database.preference.KeyValueEntity
import com.faddy.singboxsdk.database.preference.RoomPreferenceDataStore
import com.faddy.singboxsdk.di.UsedServices
import org.json.JSONObject
import java.io.File

object Settings {
    private lateinit var services: UsedServices
    private lateinit var keyValueDatabase: KeyValueEntity.Dao
    fun init(passedServices: UsedServices, passedKeyValueDatabase: KeyValueEntity.Dao) {
        services = passedServices
        keyValueDatabase = passedKeyValueDatabase

        dataStore = RoomPreferenceDataStore(keyValueDatabase)
        selectedProfile = dataStore.getLong(SettingsKey.SELECTED_PROFILE) ?: 1L
        serviceMode = dataStore.getString(SettingsKey.SERVICE_MODE) ?: ServiceMode.NORMAL
        startedByUser = dataStore.getBoolean(SettingsKey.STARTED_BY_USER) ?: false

        disableMemoryLimit = dataStore.getBoolean(SettingsKey.DISABLE_MEMORY_LIMIT) ?: false
        dynamicNotification = dataStore.getBoolean(SettingsKey.DYNAMIC_NOTIFICATION) ?: true

        perAppProxyEnabled = dataStore.getBoolean(SettingsKey.PER_APP_PROXY_ENABLED) ?: false
        perAppProxyMode = dataStore.getInt(SettingsKey.PER_APP_PROXY_MODE) ?: PER_APP_PROXY_EXCLUDE
        perAppProxyList = dataStore.getStringSet(SettingsKey.PER_APP_PROXY_LIST) ?: setOf<String>()

        systemProxyEnabled = dataStore.getBoolean(SettingsKey.SYSTEM_PROXY_ENABLED) ?: true

    }

    lateinit var dataStore: RoomPreferenceDataStore
    var selectedProfile = 0L
    var serviceMode = ServiceMode.NORMAL
    var startedByUser = false

    var disableMemoryLimit = false
    var dynamicNotification = true

    var perAppProxyEnabled = false
    var perAppProxyMode = 1
    var perAppProxyList = setOf<String>()

    var systemProxyEnabled = true

    const val PER_APP_PROXY_DISABLED = 0
    const val PER_APP_PROXY_EXCLUDE = 1
    const val PER_APP_PROXY_INCLUDE = 2

    fun serviceClass(): Class<*> {
        return when (serviceMode) {
            ServiceMode.VPN -> VPNService::class.java
            else -> ProxyService::class.java
        }
    }

    suspend fun rebuildServiceMode(): Boolean {
        var newMode = ServiceMode.NORMAL
        try {
            if (needVPNService()) {
                newMode = ServiceMode.VPN
            }
        } catch (_: Exception) {
        }
        if (serviceMode == newMode) {
            return false
        }
        serviceMode = newMode
        return true
    }

    private suspend fun needVPNService(): Boolean {
        val selectedProfileId = selectedProfile
        if (selectedProfileId == -1L) return false
        val profile = ProfileManager.get(selectedProfile) ?: return false
        val content = JSONObject(File(profile.typed.path).readText())
        val inbounds = content.getJSONArray("inbounds")
        for (index in 0 until inbounds.length()) {
            val inbound = inbounds.getJSONObject(index)
            if (inbound.getString("type") == "tun") {
                return true
            }
        }
        return false
    }
}