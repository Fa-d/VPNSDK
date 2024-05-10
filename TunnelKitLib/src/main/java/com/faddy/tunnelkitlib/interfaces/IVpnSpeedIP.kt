package com.faddy.tunnelkitlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.tunnelkitlib.model.VPNType

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
}

interface IVpnSpeedIPTyped {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>
}