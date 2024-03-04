package com.faddy.motherlib.interfaces

import com.faddy.motherlib.model.VPNType

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

