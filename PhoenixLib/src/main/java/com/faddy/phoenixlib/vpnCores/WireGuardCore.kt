package com.faddy.phoenixlib.vpnCores

import android.content.ComponentName
import android.content.Context
import android.service.quicksettings.TileService
import com.faddy.wgtunlib.data.model.TunnelConfig
import com.faddy.wgtunlib.service.foreground.ServiceManager
import com.faddy.wgtunlib.service.tile.TunnelControlTile
import com.faddy.wgtunlib.service.tunnel.VpnService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class WireGuardCore {

    @Inject
    lateinit var vpnService: VpnService
    fun requestListeningState(passedContext: Context) {
        TileService.requestListeningState(
            passedContext,
            ComponentName(passedContext, TunnelControlTile::class.java),
        )
        //vpnService.vpnState
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
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = OK8m04WBsQvuq0Tb3zj7ZAxLkeVTrprZedHaUrTdRFU=\n\n[Peer]\nPublicKey = bdOcBvDqwdykOi5A1fC2VnROJNhv3+iw0dTkgx5jPQk=\nPresharedKey = cMy4mUafmwJ4+Z5LGuHGFzAYY0FYrMUyHOjEu4/k0pI=\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 134.122.54.172:51820\nPersistentKeepalive = 25"
    val wgTunnelName = "name"
}