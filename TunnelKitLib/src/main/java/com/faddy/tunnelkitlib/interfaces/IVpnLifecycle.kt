package com.faddy.tunnelkitlib.interfaces

import com.faddy.tunnelkitlib.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}

interface IVpnLifecycleTyped {
    fun onVPNStart(vpnType: VPNType)
    fun onVPNResume(vpnType: VPNType)
    fun onVPNDestroy(vpnType: VPNType)
    fun onVPNPause(vpnType: VPNType)
}

