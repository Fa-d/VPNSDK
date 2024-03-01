package com.faddy.motherlib.interfaces

import androidx.lifecycle.LiveData

interface IVpnSpeedIP {
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>
}