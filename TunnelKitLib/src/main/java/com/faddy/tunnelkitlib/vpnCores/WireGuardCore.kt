package com.faddy.tunnelkitlib.vpnCores

import android.content.ComponentName
import android.content.Context
import android.service.quicksettings.TileService
import com.faddy.wgtunlib.data.model.TunnelConfig
import com.faddy.wgtunlib.service.foreground.ServiceManager
import com.faddy.wgtunlib.service.tile.TunnelControlTile
import com.faddy.wgtunlib.service.tunnel.VpnService
import dagger.hilt.EntryPoint
import javax.inject.Inject

@EntryPoint
class WireGuardCore {

    @Inject
    lateinit var vpnService: VpnService
    fun requestListeningState(passedContext: Context) {
        TileService.requestListeningState(
            passedContext,
            ComponentName(passedContext, TunnelControlTile::class.java),
        )
        vpnService.vpnState
    }


    fun startVpnService(passedContext: Context) {
        ServiceManager.startVpnService(
            passedContext,
            TunnelConfig(id = 0, name = wgTunnelName, wgQuick = wgTunnelConfig).toString()
        )
    }

    fun stopVpnService(passedContext: Context) {
        ServiceManager.stopVpnService(passedContext)
    }


    val wgTunnelConfig =
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = YOUR_PRIVATE_KEY_HERE\n\n[Peer]\nPublicKey = YOUR_PUBLIC_KEY_HERE\nPresharedKey = YOUR_PRESHARED_KEY_HERE\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 10.0.0.2:51820\nPersistentKeepalive = 25"
    val wgTunnelName = "name"
}