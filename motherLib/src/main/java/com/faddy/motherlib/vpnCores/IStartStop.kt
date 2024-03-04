package com.faddy.motherlib.vpnCores

import com.faddy.motherlib.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile)
    fun stopVpn()
}