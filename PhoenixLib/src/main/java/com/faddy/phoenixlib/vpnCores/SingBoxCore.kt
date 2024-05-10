package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.singbox.SingBoxInternal
import com.faddy.singbox.bg.BoxService
import com.faddy.singbox.bg.ServiceConnection
import com.faddy.singbox.constant.Status
import io.nekohasekai.sfa.VPNService
import javax.inject.Inject

class SingBoxCore @Inject constructor(private val appContext: Context) :
    ServiceConnection.Callback, IVpnSpeedIP, IVpnLifecycle, IStartStop {
    val serviceStatus = MutableLiveData(Status.Stopped)
    val internalClass = SingBoxInternal()
    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)
    private var connection: ServiceConnection? = null

    override fun onServiceStatusChanged(status: Status) {
        serviceStatus.postValue(status)
        Log.e("serviceStatus", status.name)
        when (status) {
            Status.Stopped -> {
                currentVpnState.postValue(VPNStatus.DISCONNECTED)
            }

            Status.Starting -> {
                currentVpnState.postValue(VPNStatus.CONNECTING)
            }

            Status.Started -> {
                currentVpnState.postValue(VPNStatus.CONNECTED)
            }

            Status.Stopping -> {
                currentVpnState.postValue(VPNStatus.DISCONNECTING)
            }
        }
    }


    private fun reconnect() {
        connection?.reconnect()
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        connection = ServiceConnection(appContext, this)
        reconnect()
        val intent = Intent(appContext, VPNService::class.java)
        if (!vpnProfile.vpnConfig.isNullOrEmpty()) {
            intent.putExtra("daad", vpnProfile.vpnConfig)
            ContextCompat.startForegroundService(appContext, intent)
        }
    }

    override fun stopVpn() {
        BoxService.stop()
    }

    override fun onVPNStart() {
    }

    override fun onVpnCreate() {
        reconnect()
    }

    override fun onVPNResume() {
        connection = ServiceConnection(appContext, this)
        reconnect()
    }

    override fun onVPNDestroy() {
        connection?.disconnect()
        internalClass.statusClient.connect()
    }

    override fun onVPNPause() {
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return internalClass.uploadDiff
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return internalClass.downloadDiff
    }
    override fun getCurrentIp(): LiveData<String> {
        return liveData { }
    }

}