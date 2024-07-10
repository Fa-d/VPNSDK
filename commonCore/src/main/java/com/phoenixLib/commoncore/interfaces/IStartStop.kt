package com.phoenixLib.commoncore.interfaces

import android.app.Activity
import android.content.Context
import com.phoenixLib.commoncore.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile, passedContext: Activity)
    fun stopVpn()
}