package com.faddy.wgtunlib.main

import com.faddy.wgtunlib.data.model.Settings
import com.faddy.wgtunlib.service.tunnel.VpnState
import com.faddy.wgtunlib.util.TunnelConfigs

data class MainUiState(
    val settings: Settings = Settings(),
    val tunnels: TunnelConfigs = emptyList(),
    val vpnState: VpnState = VpnState(),
    val loading: Boolean = true
)
