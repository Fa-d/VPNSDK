package com.faddy.vpnsdk.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.vpnsdk.model.VPNStatus

interface IVpnStatus {
    fun getVpnConnectedStatus(): LiveData<VPNStatus>
    fun getConnectedTime(): LiveData<String>
    fun isVpnConnected(): Boolean
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity)
}