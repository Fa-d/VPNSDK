package com.faddy.motherlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData
import com.faddy.motherlib.model.VPNStatus

interface IVpnStatus {
    fun getVpnConnectedStatus(): LiveData<VPNStatus>
    fun getConnectedTime(): LiveData<String>
    fun isVpnConnected(): Boolean
    fun setVpnStatus(vpnState: VPNStatus)
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity)
}