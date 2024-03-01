package com.faddy.tunnelkitlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.tunnelkitlib.model.VpnProfile
import com.faddy.tunnelkitlib.utils.VPNStatus
import com.faddy.tunnelkitlib.utils.VPNType

interface ICoreVpn {
    fun startConnect(type: VPNType, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun getVpnType(): LiveData<VPNType>
}