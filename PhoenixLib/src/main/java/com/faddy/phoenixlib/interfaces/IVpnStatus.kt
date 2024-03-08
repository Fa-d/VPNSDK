package com.faddy.phoenixlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.phoenixlib.model.VPNStatus

interface IVpnStatus {
    fun getVpnConnectedStatus(): LiveData<VPNStatus>
    fun getConnectedTime(): LiveData<String>
    fun isVpnConnected(): Boolean
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity)
}