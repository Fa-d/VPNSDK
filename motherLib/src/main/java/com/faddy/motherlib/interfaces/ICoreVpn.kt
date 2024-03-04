package com.faddy.motherlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun getVpnType(): LiveData<VPNType>
    fun setVpnType(vpnType: VPNType)
    fun setVpnListeners()
}