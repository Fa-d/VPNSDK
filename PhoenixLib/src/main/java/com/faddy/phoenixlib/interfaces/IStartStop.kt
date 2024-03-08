package com.faddy.phoenixlib.interfaces

import android.app.Activity
import android.content.Context
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile, passedContext: Activity)
    fun stopVpn(vpnProfile: VPNType, passedContext: Context)
}