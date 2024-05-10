package com.faddy.phoenixlib.interfaces

import com.faddy.phoenixlib.model.VPNType

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

