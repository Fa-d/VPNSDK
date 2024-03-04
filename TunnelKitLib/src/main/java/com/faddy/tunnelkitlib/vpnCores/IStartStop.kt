package com.faddy.tunnelkitlib.vpnCores

import com.faddy.tunnelkitlib.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile)
    fun stopVpn()
}