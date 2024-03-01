package com.faddy.motherlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.utils.VPNStatus
import com.faddy.motherlib.utils.VPNType

interface ICoreVpn {
    fun startConnect(type: VPNType, vpnProfile: VpnProfile): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun getVpnType(): LiveData<VPNType>
}