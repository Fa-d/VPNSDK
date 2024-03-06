package com.faddy.motherlib.interfaces

import android.app.Activity
import androidx.lifecycle.LiveData

interface IVpnStatus {
    fun getVpnConnectedStatus()
    fun getConnectedTime(): LiveData<String>
    fun isVpnConnected(): Boolean
    fun isVpnServicePrepared(): Boolean
    fun prepareVPNService(context: Activity)
}