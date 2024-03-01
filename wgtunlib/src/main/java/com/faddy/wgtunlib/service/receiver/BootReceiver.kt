package com.faddy.wgtunlib.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.faddy.wgtunlib.data.repository.SettingsRepository
import com.faddy.wgtunlib.data.repository.TunnelConfigRepository
import com.faddy.wgtunlib.service.foreground.ServiceManager
import com.faddy.wgtunlib.util.goAsync
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var tunnelConfigRepository: TunnelConfigRepository

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (Intent.ACTION_BOOT_COMPLETED != intent?.action) return@goAsync
        val settings = settingsRepository.getSettings()
        if (settings.isAutoTunnelEnabled) {
            ServiceManager.startWatcherServiceForeground(context!!)
        } else if (settings.isAlwaysOnVpnEnabled) {
            ServiceManager.startVpnServicePrimaryTunnel(
                context!!, settings, tunnelConfigRepository.getAll().firstOrNull()
            )
        }
    }
}
