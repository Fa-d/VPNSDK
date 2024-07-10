package com.phoenixLib.commoncore.interfaces

import android.app.Activity
import com.phoenixLib.commoncore.model.VPNType

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume(activity: Activity)
    fun onVPNDestroy()
    fun onVPNPause()
}

