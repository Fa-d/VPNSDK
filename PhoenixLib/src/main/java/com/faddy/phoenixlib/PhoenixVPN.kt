package com.faddy.phoenixlib

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.faddy.phoenixlib.interfaces.ICoreVpn
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.interfaces.IVpnStatus
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.phoenixlib.utils.CountdownTimerService
import com.faddy.phoenixlib.utils.SessionManagerInternal
import com.faddy.phoenixlib.utils.ping
import com.faddy.phoenixlib.utils.toMutableLiveData
import com.faddy.phoenixlib.vpnCores.VpnSwitchFactory
import com.faddy.singbox.CustomApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import javax.inject.Inject


class PhoenixVPN @Inject constructor(
    private val vpnSwitchFactory: VpnSwitchFactory,
    private val phoenixContext: Context,
    private val customApplication: CustomApplication,
    private val internalSession: SessionManagerInternal,
    private val internalSetup: Int
) : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {


    var connectedVpnTime = MutableLiveData("00:00:00")
    var currentPing = MutableLiveData("0")
    private val totalUploadSpeed = MutableLiveData(0L)
    private val totalDownloadSpeed = MutableLiveData(0L)
    var currentUploadSpeed: MutableLiveData<Long>? = null
    var currentDownloadSpeed: MutableLiveData<Long>? = null
    var connectedStatus: MutableLiveData<VPNStatus>? = null
    var myCurrentIp: MutableLiveData<String>? = null
    var funInvoker: (() -> Unit)? = null

    private fun uploadDownloadLitener() {
        currentUploadSpeed = vpnSwitchFactory.getDownloadSpeed().toMutableLiveData()
        currentDownloadSpeed = vpnSwitchFactory.getUploadSpeed().toMutableLiveData()
    }

    private fun resetVpnListeners() {
        uploadDownloadLitener()
        connectedStatus = vpnSwitchFactory.setVpnStateListeners().toMutableLiveData()
        myCurrentIp = vpnSwitchFactory.getCurrentIp().toMutableLiveData()

    }
    override fun startConnect(
        passedActivity: Activity, vpnProfile: VpnProfile
    ): LiveData<VPNStatus> {
        setVpnType(vpnProfile)
        getVpnConnectedStatus()
        if (!isVpnConnected()) {
            startTimerService(passedActivity)
            vpnSwitchFactory.startVpn(vpnProfile, passedActivity)
        } else {
            disconnect()
        }
        resetVpnListeners()
        funInvoker?.invoke()

        return vpnSwitchFactory.setVpnStateListeners()
    }

    override fun disconnect(): LiveData<VPNStatus> {
        vpnSwitchFactory.stopVpn()
        stopTimerService(phoenixContext)
        currentUploadSpeed?.postValue(0L)
        currentDownloadSpeed?.postValue(0L)
        connectedStatus?.postValue(VPNStatus.DISCONNECTED)
        myCurrentIp?.postValue("0.0.0.0")
        connectedVpnTime.postValue("00:00:00")
        currentPing.postValue("0")
        funInvoker?.invoke()
        return vpnSwitchFactory.setVpnStateListeners()
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return vpnSwitchFactory.setVpnStateListeners()
    }

    override fun setVpnType(vpnProfile: VpnProfile) {
        internalSession.setLastConnVpnType(vpnProfile.vpnType.name)
        internalSession.setLastConnServerIP(vpnProfile.serverIP.split(":")[0])
    }
    override fun getConnectedTime(): LiveData<String> {
        return connectedVpnTime
    }

    override fun isVpnConnected(): Boolean {
        return vpnSwitchFactory.setVpnStateListeners().value == VPNStatus.CONNECTED
    }

    override fun isVpnServicePrepared(): Boolean {
        return VpnService.prepare(phoenixContext) == null
    }

    override fun prepareVPNService(context: Activity, activityResLun: ActivityResultLauncher<Intent>) {
        val vpnIntent = VpnService.prepare(context)
        if (vpnIntent != null) {
            activityResLun.launch(vpnIntent)
            //context.startActivityForResult(vpnIntent, 1000)
        }
    }

    override fun onVPNStart() {
        vpnSwitchFactory.onVPNStart()
    }

    override fun onVpnCreate() {
        vpnSwitchFactory.onVpnCreate()
    }

    override fun onVPNResume() {
        vpnSwitchFactory.onVPNResume()
        resetVpnListeners()
        LocalBroadcastManager.getInstance(phoenixContext)
            .registerReceiver(receiver, IntentFilter(CountdownTimerService.TIME_INFO));
        funInvoker?.invoke()
    }

    override fun onVPNDestroy() {
        vpnSwitchFactory.onVPNDestroy()
    }

    override fun onVPNPause() {
        vpnSwitchFactory.onVPNPause()
        LocalBroadcastManager.getInstance(phoenixContext).unregisterReceiver(receiver);
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return vpnSwitchFactory.getUploadSpeed()
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return vpnSwitchFactory.getDownloadSpeed()
    }

    override fun getCurrentIp(): LiveData<String> {
        return vpnSwitchFactory.getCurrentIp()
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
                        myCurrentIp?.postValue(getIPAddress(true))
                        connectedStatus?.postValue(VPNStatus.DISCONNECTED)
                    } else {
                        getUploadSpeed()
                        getDownloadSpeed()
                        getPingCurrentServer()
                        uploadDownloadLitener()
                        myCurrentIp?.postValue(internalSession.getLastConnServerIP())
                        connectedVpnTime.postValue(intent.getStringExtra("VALUE"))
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
                currentPing.postValue(pingStatus.replace(" ms", ""))
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