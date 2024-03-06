package com.faddy.motherlib.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume(passedContext: Context)
    fun onVPNDestroy()
    fun onVPNPause()
}

interface IVpnLifecycleTyped {
    fun setVpnStateListeners(vpnType: VPNType): LiveData<VPNStatus>
    fun onVPNStart(vpnType: VPNType)
    fun onVPNResume(vpnType: VPNType, passedContext: Context)
    fun onVPNDestroy(vpnType: VPNType)
    fun onVPNPause(vpnType: VPNType)
}

