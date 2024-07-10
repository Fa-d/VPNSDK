package com.faddy.singbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.faddy.singbox.bg.BoxService
import com.faddy.singbox.bg.ServiceConnection
import com.faddy.singbox.bg.VPNService
import com.faddy.singbox.constant.Status
import com.phoenixLib.commoncore.interfaces.IStartStop
import com.phoenixLib.commoncore.interfaces.IVpnLifecycle
import com.phoenixLib.commoncore.interfaces.IVpnSpeedIP
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VpnProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingBoxCore @Inject constructor(
    private val appContext: Context,
    private val customApplication: CustomApplication,
) : ServiceConnection.Callback, IVpnSpeedIP, IVpnLifecycle, IStartStop {
    val serviceStatus = MutableLiveData(Status.Stopped)
    val internalClass = SingBoxInternal()
    val currentVpnState = MutableStateFlow(VPNStatus.DISCONNECTED)
    private var connection: ServiceConnection? = null

    override fun onServiceStatusChanged(status: Status) {
        serviceStatus.postValue(status)
        CoroutineScope(Dispatchers.Main).launch {
            when (status) {
                Status.Stopped -> { currentVpnState.emit(VPNStatus.DISCONNECTED) }
                Status.Starting -> { currentVpnState.emit(VPNStatus.CONNECTING) }
                Status.Started -> { currentVpnState.emit(VPNStatus.CONNECTED) }
                Status.Stopping -> { currentVpnState.emit(VPNStatus.DISCONNECTING) }
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

    override fun onVPNResume(activity: Activity) {
        connection = ServiceConnection(appContext, this)
        reconnect()
    }

    override fun onVPNDestroy() {
        connection?.disconnect()
        internalClass.statusClient.connect()
    }

    override fun onVPNPause() {
    }

    override fun getUploadSpeed(): Flow<Long> {
        return internalClass.uploadDiff
    }

    override fun getDownloadSpeed(): Flow<Long> {
        return internalClass.downloadDiff
    }

    override fun getCurrentIp(): Flow<String> {
        return flow { }
    }

}