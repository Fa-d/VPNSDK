package com.faddy.tunnelkitlib.interfaces

import com.faddy.tunnelkitlib.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVpnCreate()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}

interface IVpnLifecycleTyped {
    fun onVPNStart()

    fun onVpnCreate()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}

