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
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import javax.inject.Inject


class PhoenixVPNModified @Inject constructor(
    private val phoenixContext: Context,
    private val internalSession: SessionManagerInternal,
    private val internalSetup: Int
) : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {
    /*    lateinit var dynamicClass: Class<*>
        lateinit var constructor: Constructor<*>
        lateinit var vpnFactory: Any*/
    var connectedVpnTime = MutableStateFlow("00:00:00")
    var currentPing = MutableStateFlow("0")
    private val totalUploadSpeed = MutableStateFlow(0L)
    private val totalDownloadSpeed = MutableStateFlow(0L)
    var currentUploadSpeed = MutableStateFlow(0L)
    var currentDownloadSpeed = MutableStateFlow(0L)
    var connectedStatus = MutableStateFlow(VPNStatus.DISCONNECTED)
    var myCurrentIp = MutableStateFlow("8.8.8.8")
    var funInvoker: (() -> Unit) = fun() {}

    private fun uploadDownloadLitener() {
        /*   CoroutineScope(Dispatchers.Main).launch {
               val fieldCurrentUploadSpeed =
                   dynamicClass.getDeclaredField("currentUploadSpeed").apply {
                       isAccessible = true
                   }
               (fieldCurrentUploadSpeed.get(vpnFactory) as MutableStateFlow<*>).collect {
                   currentUploadSpeed.emit(it as Long)
               }
           }
           CoroutineScope(Dispatchers.Main).launch {
               val fieldCurrentDownloadSpeed =
                   dynamicClass.getDeclaredField("currentDownloadSpeed").apply {
                       isAccessible = true
                   }
               (fieldCurrentDownloadSpeed.get(vpnFactory) as MutableStateFlow<*>).collect {
                   currentDownloadSpeed.emit(it as Long)
               }
           }*/
    }

    private fun resetVpnListeners() {
        uploadDownloadLitener()
        /*CoroutineScope(Dispatchers.Main).launch {
            val getLiveDataMethod = dynamicClass.getDeclaredMethod("getVpnState").invoke(vpnFactory)
            connectedStatus = getLiveDataMethod as MutableStateFlow<VPNStatus>

            Log.e("resetVpnListeners", connectedStatus.value.toString())

            getLiveDataMethod.collect {
                Log.e("collectLatest", it.toString())
            }
            val fieldConnectedStatus = dynamicClass.getDeclaredField("currentVpnState").apply {
                isAccessible = true
            }

            val fieldMyCurrentIp = dynamicClass.getDeclaredField("currentIp").apply {
                isAccessible = true
            }
            (fieldMyCurrentIp.get(vpnFactory) as MutableStateFlow<*>).collect {
                myCurrentIp.emit(it.toString())
            }
            (fieldConnectedStatus.get(vpnFactory) as MutableStateFlow<*>).collect {
                connectedStatus.emit(it as VPNStatus)
            }
        }*/
    }
    override fun startConnect(
        passedActivity: Activity, vpnProfile: VpnProfile
    ): LiveData<VPNStatus> {
        setVpnType(vpnProfile)
        getVpnConnectedStatus()
        if (!isVpnConnected()) {
            startTimerService(passedActivity)
            /*  val startVpnMethod: Method = dynamicClass.getDeclaredMethod(
                  "startVpn", VpnProfile::class.java, Activity::class.java
              )
              startVpnMethod.invoke(vpnFactory, vpnProfile, passedActivity)*/
        } else {
            disconnect()
        }
        resetVpnListeners()
        funInvoker.invoke()

        return liveData { }
    }

    override fun disconnect(): LiveData<VPNStatus> {
        // dynamicClass.getDeclaredMethod("stopVpn").invoke(vpnFactory)
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

    override fun prepareVPNService(context: Activity, activityResLun: ActivityResultLauncher<Intent>) {
        val vpnIntent = VpnService.prepare(context)
        if (vpnIntent != null) {
            activityResLun.launch(vpnIntent)
        }
    }

    override fun onVPNStart() {
        // vpnFacInstance.javaClass.getDeclaredMethod("onVPNStart")
    }


    override fun onVPNResume(activity: Activity) {
        /*        dynamicClass = Class.forName("de.blinkt.OpenVpnCore")
                constructor = dynamicClass.getDeclaredConstructor(Context::class.java)
                vpnFactory = constructor.newInstance(phoenixContext)*/
        resetVpnListeners()
        LocalBroadcastManager.getInstance(phoenixContext)
            .registerReceiver(receiver, IntentFilter(CountdownTimerService.TIME_INFO));
        funInvoker.invoke()
    }

    override fun onVPNDestroy() {}

    override fun onVPNPause() {
        LocalBroadcastManager.getInstance(phoenixContext).unregisterReceiver(receiver);
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

    fun startTimerService(passedActivity: Activity) {
        val intent = Intent(passedActivity, CountdownTimerService::class.java)
        passedActivity.startService(intent)
    }

    fun stopTimerService(passedActivity: Context) {
        val intent = Intent(passedActivity, CountdownTimerService::class.java)
        passedActivity.stopService(intent)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == CountdownTimerService.TIME_INFO) {
                if (intent.hasExtra("VALUE")) {
                    if (intent.getStringExtra("VALUE") == "Stopped") {
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

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0, delim
                                ).uppercase(
                                    Locale.getDefault()
                                )
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return "8.8.8.8"
    }
}