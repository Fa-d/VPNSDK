package com.faddy.phoenixlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun setVpnType(vpnType: VpnProfile)
}