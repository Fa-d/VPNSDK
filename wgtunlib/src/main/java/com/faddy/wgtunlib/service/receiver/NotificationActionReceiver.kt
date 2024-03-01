package com.faddy.wgtunlib.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.faddy.wgtunlib.data.repository.SettingsRepository
import com.faddy.wgtunlib.service.foreground.ServiceManager
import com.faddy.wgtunlib.util.Constants
import com.faddy.wgtunlib.util.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context, intent: Intent?) = goAsync {
        try {
            val settings = settingsRepository.getSettings()
            if (settings.defaultTunnel != null) {
                ServiceManager.stopVpnService(context)
                delay(Constants.TOGGLE_TUNNEL_DELAY)
                ServiceManager.startVpnServiceForeground(context, settings.defaultTunnel.toString())
            }
        } finally {
            cancel()
        }
    }
}
