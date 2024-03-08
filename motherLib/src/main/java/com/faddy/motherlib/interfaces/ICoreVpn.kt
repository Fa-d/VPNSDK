package com.faddy.motherlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VpnProfile

interface ICoreVpn {
    fun startConnect(passedActivity: Activity, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun setVpnType(vpnType: VpnProfile)
}