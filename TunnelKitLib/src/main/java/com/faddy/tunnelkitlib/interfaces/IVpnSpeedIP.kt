package com.faddy.tunnelkitlib.interfaces

import androidx.lifecycle.LiveData

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>
}