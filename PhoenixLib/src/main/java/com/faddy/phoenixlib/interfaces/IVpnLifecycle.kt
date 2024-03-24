package com.faddy.phoenixlib.interfaces

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.faddy.phoenixlib.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVpnCreate(passedContext: Context, lifecycleObserver: LifecycleOwner)
    fun onVPNResume(passedContext: Context)
    fun onVPNDestroy()
    fun onVPNPause()
}

interface IVpnLifecycleTyped {
    fun onVPNStart(vpnType: VPNType)

    fun onVpnCreate(passedContext: Context, lifecycleObserver: LifecycleOwner)
    fun onVPNResume(vpnType: VPNType, passedContext: Context)
    fun onVPNDestroy(vpnType: VPNType)
    fun onVPNPause(vpnType: VPNType)
}

