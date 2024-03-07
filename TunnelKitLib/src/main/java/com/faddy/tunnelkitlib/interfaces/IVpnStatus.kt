package com.faddy.tunnelkitlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.tunnelkitlib.model.VPNStatus

interface IVpnStatus {
    fun getVpnConnectedStatus(): LiveData<VPNStatus>
    fun getConnectedTime(): LiveData<String>
    fun isVpnConnected(): Boolean
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity)
}