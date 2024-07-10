package com.phoenixLib.commoncore.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VpnProfile

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: com.phoenixLib.commoncore.model.VpnProfile): LiveData<com.phoenixLib.commoncore.model.VPNStatus>
    fun disconnect(): LiveData<com.phoenixLib.commoncore.model.VPNStatus>
    fun setVpnType(vpnType: com.phoenixLib.commoncore.model.VpnProfile)
}