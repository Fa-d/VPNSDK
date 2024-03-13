package com.faddy.wgtunlib.service.tunnel

import com.wireguard.android.backend.Tunnel
import com.faddy.wgtunlib.data.model.TunnelConfig
import kotlinx.coroutines.flow.StateFlow

interface VpnService : Tunnel {
    suspend fun startTunnel(tunnelConfig: TunnelConfig): Tunnel.State

    suspend fun stopTunnel()

    val vpnServiceState: StateFlow<VpnState>

    fun getState(): Tunnel.State
}
