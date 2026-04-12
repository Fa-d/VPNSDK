package com.faddy.vpnsdk.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.vpnsdk.model.VPNStatus
import com.faddy.vpnsdk.model.VpnProfile

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun setVpnType(vpnType: VpnProfile)
}