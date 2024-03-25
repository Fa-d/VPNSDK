package com.faddy.wgtunlib.service

import com.wireguard.android.backend.Tunnel
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.data.VpnState
import kotlinx.coroutines.flow.StateFlow

interface VpnService : Tunnel {
    suspend fun startTunnel(tunnelConfig: TunnelConfig): Tunnel.State

    suspend fun stopTunnel()

    val vpnServiceState: StateFlow<VpnState>

    fun getState(): Tunnel.State
}
