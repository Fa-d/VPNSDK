package com.faddy.motherlib.model

import com.faddy.motherlib.utils.VPNType

data class VpnProfile(
    val vpnType: VPNType, val userName: String, val password: String
)