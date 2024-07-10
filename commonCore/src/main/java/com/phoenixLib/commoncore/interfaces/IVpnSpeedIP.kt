package com.phoenixLib.commoncore.interfaces

import kotlinx.coroutines.flow.Flow

interface IVpnSpeedIP {
    fun getUploadSpeed(): Flow<Long>
    fun getDownloadSpeed(): Flow<Long>
    fun getCurrentIp(): Flow<String>

}
