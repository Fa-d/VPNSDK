package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.singbox.SingBoxInternal
import com.faddy.singbox.bg.BoxService
import com.faddy.singbox.bg.ServiceConnection
import com.faddy.singbox.constant.Status
import com.faddy.singbox.database.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingBoxCore : ServiceConnection.Callback, IVpnSpeedIP, IVpnLifecycle, IStartStop {
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


    fun reconnect(lifecycleOwner: LifecycleOwner? = null) {
        connection?.reconnect()
        if (lifecycleOwner != null) {
            serviceStatus.observe(lifecycleOwner) {
                when (it) {
                    Status.Stopped -> {
                        currentVpnState.postValue(VPNStatus.DISCONNECTED)
                    }

                    Status.Starting -> {
                        currentVpnState.postValue(VPNStatus.CONNECTING)
                    }

                    Status.Started -> {
                        internalClass.statusClient.connect()
                        currentVpnState.postValue(VPNStatus.CONNECTED)
                    }

                    Status.Stopping -> {
                        currentVpnState.postValue(VPNStatus.DISCONNECTING)
                    }

                    else -> {}
                }
            }
        }

    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        connection = ServiceConnection(passedContext, this)
        CoroutineScope(Dispatchers.IO).launch {
            if (Settings.rebuildServiceMode()) {
                reconnect()
            }
            val intent = Intent(passedContext, Settings.serviceClass())
            if (!vpnProfile.vpnConfig.isNullOrEmpty()) {
                intent.putExtra("daad", vpnProfile.vpnConfig)
               // intent.putExtra("filePath", vpnProfile.vpnConfig)
                withContext(Dispatchers.Main) {
                    ContextCompat.startForegroundService(passedContext, intent)
                }
            }
        }
    }

    override fun stopVpn(vpnProfile: VPNType, passedContext: Context) {
        BoxService.stop()
    }

    override fun onVPNStart() {
    }

    override fun onVpnCreate(passedContext: Context, lifecycleObserver: LifecycleOwner) {
        reconnect(lifecycleObserver)
    }

    override fun onVPNResume(passedContext: Context) {
        connection = ServiceConnection(passedContext, this)
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

}