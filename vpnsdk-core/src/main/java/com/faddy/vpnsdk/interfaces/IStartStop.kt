package com.faddy.vpnsdk.interfaces

import android.app.Activity
import android.content.Context
import com.faddy.vpnsdk.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile, passedContext: Activity)
    fun stopVpn()
}