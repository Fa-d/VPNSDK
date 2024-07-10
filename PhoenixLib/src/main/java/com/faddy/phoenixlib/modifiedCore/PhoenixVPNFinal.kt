package com.faddy.phoenixlib.modifiedCore

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.faddy.phoenixlib.utils.CountdownTimerService
import com.faddy.phoenixlib.utils.SessionManagerInternal
import com.faddy.phoenixlib.utils.getIPAddress
import com.faddy.phoenixlib.utils.ping
import com.phoenixLib.commoncore.interfaces.ICoreVpn
import com.phoenixLib.commoncore.interfaces.IVpnLifecycle
import com.phoenixLib.commoncore.interfaces.IVpnSpeedIP
import com.phoenixLib.commoncore.interfaces.IVpnStatus
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VpnProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class PhoenixVPNFinal @Inject constructor(
    private val phoenixContext: Context,
    private val internalSession: SessionManagerInternal,
    private val internalSetup: Int,
    private val localBroadcastManager: LocalBroadcastManager
) : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {

    var connectedVpnTime = MutableStateFlow("00:00:00")
    var currentPing = MutableStateFlow("0")
    private val totalUploadSpeed = MutableStateFlow(0L)
    private val totalDownloadSpeed = MutableStateFlow(0L)
    var currentUploadSpeed = MutableStateFlow(0L)
    var currentDownloadSpeed = MutableStateFlow(0L)
    var connectedStatus = MutableStateFlow(VPNStatus.DISCONNECTED)
    var myCurrentIp = MutableStateFlow("8.8.8.8")
    var funInvoker: (() -> Unit) = fun() {}

    private fun uploadDownloadLitener() {}

    private fun resetVpnListeners() {
        uploadDownloadLitener()
    }

    override fun startConnect(
        passedActivity: Activity, vpnProfile: VpnProfile
    ): LiveData<VPNStatus> {
        setVpnType(vpnProfile)
        getVpnConnectedStatus()
        localBroadcastManager.sendBroadcast(
            Intent("conDisReceiver").putExtra("conDisReceiver", "startConnect")
        )
        if (!isVpnConnected()) {
            startTimerService(passedActivity)
        } else {
            disconnect()
        }
        resetVpnListeners()
        funInvoker.invoke()

        return liveData { }
    }

    override fun disconnect(): LiveData<VPNStatus> {
        localBroadcastManager.sendBroadcast(
            Intent("conDisReceiver").putExtra("conDisReceiver", "stopVpn")
        )
        stopTimerService(phoenixContext)
        CoroutineScope(Dispatchers.Main).launch {
            currentUploadSpeed.emit(0L)
            currentDownloadSpeed.emit(0L)
            connectedStatus.emit(VPNStatus.DISCONNECTED)
            myCurrentIp.emit("0.0.0.0")
            connectedVpnTime.emit("00:00:00")
            currentPing.emit("0")
            funInvoker.invoke()
        }
        return liveData { }
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return liveData { }
    }

    override fun setVpnType(vpnType: VpnProfile) {
        internalSession.setLastConnVpnType(vpnType.vpnType.name)
        internalSession.setLastConnServerIP(vpnType.serverIP.split(":")[0])
    }

    override fun getConnectedTime(): Flow<String> {
        return connectedVpnTime
    }

    override fun isVpnConnected(): Boolean {
        return connectedStatus.value == VPNStatus.CONNECTED
    }

    override fun isVpnServicePrepared(): Boolean {
        return VpnService.prepare(phoenixContext) == null
    }

    override fun prepareVPNService(
        context: Activity, activityResLun: ActivityResultLauncher<Intent>
    ) {
        val vpnIntent = VpnService.prepare(context)
        if (vpnIntent != null) {
            activityResLun.launch(vpnIntent)
        }
    }

    override fun onVPNStart() {}

    override fun onVPNResume(activity: Activity) {
        val dynamicClass = Class.forName("de.blinkt.OpenVpnCore")
        dynamicClass.getDeclaredConstructor(Context::class.java).newInstance(phoenixContext)
        resetVpnListeners()
        localBroadcastManager.registerReceiver(
            myIpTimerReciever, IntentFilter(CountdownTimerService.TIME_INFO)
        )
        localBroadcastManager.registerReceiver(
            vpnStateReceiver, IntentFilter("conState")
        )
        funInvoker.invoke()
    }

    override fun onVPNDestroy() {}

    override fun onVPNPause() {
        localBroadcastManager.unregisterReceiver(myIpTimerReciever);
        localBroadcastManager.unregisterReceiver(vpnStateReceiver);
    }

    override fun getUploadSpeed(): Flow<Long> {
        return flow { }
    }

    override fun getDownloadSpeed(): Flow<Long> {
        return flow { }
    }

    override fun getCurrentIp(): Flow<String> {
        return flow { }
    }

    private fun startTimerService(passedActivity: Activity) {
        val intent = Intent(passedActivity, CountdownTimerService::class.java)
        passedActivity.startService(intent)
    }

    private fun stopTimerService(passedActivity: Context) {
        val intent = Intent(passedActivity, CountdownTimerService::class.java)
        passedActivity.stopService(intent)
    }

    private val myIpTimerReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == CountdownTimerService.TIME_INFO) {
                if (intent.hasExtra("myIpTimerReciever")) {
                    if (intent.getStringExtra("myIpTimerReciever") == "Stopped") {
                        CoroutineScope(Dispatchers.Main).launch {
                            myCurrentIp.emit(getIPAddress(true))
                            connectedStatus.emit(VPNStatus.DISCONNECTED)
                        }
                    } else {
                        getUploadSpeed()
                        getDownloadSpeed()
                        getPingCurrentServer()
                        uploadDownloadLitener()
                        CoroutineScope(Dispatchers.Main).launch {
                            myCurrentIp.emit(internalSession.getLastConnServerIP())
                            connectedVpnTime.emit(intent.getStringExtra("VALUE") ?: "")
                        }
                    }
                }
            }
        }
    }


    private val vpnStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == "conState") {
                val vpnState = intent.getStringExtra("conState") ?: "DISCONNECTED"
                if (vpnState == "DISCONNECTED") {
                    connectedStatus.tryEmit(VPNStatus.DISCONNECTED)
                } else if (vpnState == "CONNECTED") {
                    connectedStatus.tryEmit(VPNStatus.CONNECTED)
                } else if (vpnState == "CONNECTING") {
                    connectedStatus.tryEmit(VPNStatus.CONNECTING)
                }
            }
        }
    }

    fun getPingCurrentServer() {
        var pingStatus = ""
        CoroutineScope(Dispatchers.IO).launch {
            pingStatus = ping("google.com")
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                currentPing.emit(pingStatus.replace(" ms", ""))
            }
        }
    }
}