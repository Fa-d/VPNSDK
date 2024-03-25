package com.faddy.wgtunlib.data

import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel

data class VpnState(
    val status: Tunnel.State = Tunnel.State.DOWN,
    val name: String = "",
    val statistics: Statistics? = null,
    val curTx: Long = 0L,
    val curRx: Long = 0L,
)
