package com.faddy.motherlib.interfaces

import android.app.Activity
import android.content.Context
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile

interface IStartStop {
    fun startVpn(vpnProfile: VpnProfile, passedContext: Activity)
    fun stopVpn(vpnProfile: VPNType, passedContext: Context)
}