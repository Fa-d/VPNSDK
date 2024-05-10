package com.faddy.tunnelkitlib.interfaces

import android.app.Activity
import android.content.Context
import com.faddy.tunnelkitlib.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile, passedContext: Activity)
    fun stopVpn()
}