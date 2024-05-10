package com.faddy.phoenixlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.phoenixlib.model.VPNType

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>

}
