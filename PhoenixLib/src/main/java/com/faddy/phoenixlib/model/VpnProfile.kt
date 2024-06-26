package com.faddy.phoenixlib.model

data class VpnProfile(
    val vpnType: VPNType,
    val userName: String,
    val password: String,
    val vpnConfig: String,
    val serverIP: String
)