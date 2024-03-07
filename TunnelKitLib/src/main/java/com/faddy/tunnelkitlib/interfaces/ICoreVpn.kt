package com.faddy.tunnelkitlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.tunnelkitlib.model.VpnProfile
import com.faddy.tunnelkitlib.model.VPNStatus
import com.faddy.tunnelkitlib.model.VPNType

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun setVpnType(vpnType: VPNType)
}