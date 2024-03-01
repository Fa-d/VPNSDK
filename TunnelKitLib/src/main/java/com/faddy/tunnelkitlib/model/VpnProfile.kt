package com.faddy.tunnelkitlib.model

import com.faddy.tunnelkitlib.utils.VPNType

data class VpnProfile(
    val vpnType: VPNType, val userName: String, val password: String
)