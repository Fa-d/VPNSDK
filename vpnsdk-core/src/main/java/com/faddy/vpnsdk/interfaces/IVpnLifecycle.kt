package com.faddy.vpnsdk.interfaces

import com.faddy.vpnsdk.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVpnCreate()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}

