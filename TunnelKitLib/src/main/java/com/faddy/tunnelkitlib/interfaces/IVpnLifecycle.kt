package com.faddy.tunnelkitlib.interfaces

import android.content.Context
import com.faddy.tunnelkitlib.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume(passedContext: Context)
    fun onVPNDestroy()
    fun onVPNPause()
}

interface IVpnLifecycleTyped {
    fun onVPNStart(vpnType: VPNType)
    fun onVPNResume(vpnType: VPNType, passedContext: Context)
    fun onVPNDestroy(vpnType: VPNType)
    fun onVPNPause(vpnType: VPNType)
}

