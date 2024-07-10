package com.phoenixLib.commoncore.interfaces

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import com.phoenixLib.commoncore.model.VPNStatus
import kotlinx.coroutines.flow.Flow

interface IVpnStatus {
    fun getVpnConnectedStatus(): LiveData<VPNStatus>
    fun getConnectedTime(): Flow<String>
    fun isVpnConnected(): Boolean
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity, activityResLun: ActivityResultLauncher<Intent>)
}