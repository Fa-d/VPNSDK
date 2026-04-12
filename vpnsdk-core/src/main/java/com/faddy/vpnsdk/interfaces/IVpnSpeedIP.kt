package com.faddy.vpnsdk.interfaces

import androidx.lifecycle.LiveData
import com.faddy.vpnsdk.model.VPNType

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>

}
