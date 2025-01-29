package com.faddy.phoenixlib.interfaces

import androidx.lifecycle.LiveData
import com.faddy.phoenixlib.model.VPNType

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<String>
    fun getDownloadSpeed(): LiveData<String>
    fun getCurrentIp(): LiveData<String>

}
