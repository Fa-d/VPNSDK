package com.faddy.motherlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VPNType

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
}

interface IVpnSpeedIPTyped {
    fun getUploadSpeed(vpnType: VPNType): LiveData<Long>
    fun getDownloadSpeed(vpnType: VPNType): LiveData<Long>
    fun getCurrentIp(vpnType: VPNType): LiveData<String>
}