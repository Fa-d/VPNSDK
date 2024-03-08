package com.faddy.motherlib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.faddy.motherlib.interfaces.ICoreVpn
import com.faddy.motherlib.interfaces.IVpnLifecycle
import com.faddy.motherlib.interfaces.IVpnSpeedIP
import com.faddy.motherlib.interfaces.IVpnStatus
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.service.CountdownTimerService
import com.faddy.motherlib.utils.SessionManager
import com.faddy.motherlib.utils.ping
import com.faddy.motherlib.utils.toMutableLiveData
import com.faddy.motherlib.vpnCores.VpnSwitchFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale


@SuppressLint("StaticFieldLeak")
object MotherVPN : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {
    private lateinit var motherContext: Context
    private val vpnSwitchFactory = VpnSwitchFactory()

    var connectedVpnTime = MutableLiveData("00:00:00")
    var currentPing = MutableLiveData("0")
    private val totalUploadSpeed = MutableLiveData(0L)
    private val totalDownloadSpeed = MutableLiveData(0L)
    var currentUploadSpeed: MutableLiveData<Long>? = null
    var currentDownloadSpeed: MutableLiveData<Long>? = null
    var connectedStatus: MutableLiveData<VPNStatus>? = null
    var myCurrentIp: MutableLiveData<String>? = null
    var funInvoker: (() -> Unit)? = null

    fun init(passedContext: Context): MotherVPN {
        motherContext = passedContext
        return this
    }

    fun resetVpnListeners() {
        currentUploadSpeed =
            vpnSwitchFactory.getDownloadSpeed(getLastSelectedVpn()).toMutableLiveData()
        currentDownloadSpeed =
            vpnSwitchFactory.getUploadSpeed(getLastSelectedVpn()).toMutableLiveData()
        connectedStatus =
            vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn()).toMutableLiveData()
        myCurrentIp = vpnSwitchFactory.getCurrentIp(getLastSelectedVpn()).toMutableLiveData()

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

        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun disconnect(): LiveData<VPNStatus> {
        vpnSwitchFactory.stopVpn(getLastSelectedVpn(), motherContext)
        stopTimerService(motherContext)
        currentUploadSpeed?.postValue(0L)
        currentDownloadSpeed?.postValue(0L)
        connectedStatus?.postValue(VPNStatus.DISCONNECTED)
        myCurrentIp?.postValue("0.0.0.0")
        connectedVpnTime.postValue("00:00:00")
        currentPing.postValue("0")
        funInvoker?.invoke()
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun setVpnType(vpnProfile: VpnProfile) {
        SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).setLastConnVpnType(vpnProfile.vpnType.name)
        SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).setLastConnServerIP(vpnProfile.serverIP.split(":")[0])

    }
    override fun getConnectedTime(): LiveData<String> {
        return connectedVpnTime
    }

    override fun isVpnConnected(): Boolean {
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn()).value == VPNStatus.CONNECTED
    }

    override fun isVpnServicePrepared(): Boolean {
        return VpnService.prepare(motherContext) == null
    }

    override fun prepareVPNService(context: Activity) {
        val vpnIntent = VpnService.prepare(context)
        if (vpnIntent != null) {
            context.startActivityForResult(vpnIntent, 1000)
        }
    }

    override fun onVPNStart() {
        vpnSwitchFactory.onVPNStart(getLastSelectedVpn())
    }

    override fun onVPNResume(passedContext: Context) {
        vpnSwitchFactory.onVPNResume(getLastSelectedVpn(), passedContext)
        resetVpnListeners()
        LocalBroadcastManager.getInstance(motherContext)
            .registerReceiver(receiver, IntentFilter(CountdownTimerService.TIME_INFO));
        funInvoker?.invoke()
    }

    override fun onVPNDestroy() {
        vpnSwitchFactory.onVPNDestroy(getLastSelectedVpn())
    }

    override fun onVPNPause() {
        vpnSwitchFactory.onVPNPause(getLastSelectedVpn())
        LocalBroadcastManager.getInstance(motherContext).unregisterReceiver(receiver);
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return vpnSwitchFactory.getUploadSpeed(getLastSelectedVpn())
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return vpnSwitchFactory.getDownloadSpeed(getLastSelectedVpn())
    }

    fun getCurrentIp(): LiveData<String> {
        return vpnSwitchFactory.getCurrentIp(getLastSelectedVpn())
    }

    private fun getLastSelectedVpn(): VPNType {
        val vpnState = SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).getLastConnVpnType() ?: ""
        val currentType = when (vpnState) {
            "OPENVPN" -> VPNType.OPENVPN
            "OPENCONNECT" -> VPNType.OPENCONNECT
            "WIREGUARD" -> VPNType.WIREGUARD
            "IPSECIKEV2" -> VPNType.IPSECIKEV2
            "SINGBOX" -> VPNType.SINGBOX
            else -> VPNType.NONE
        }
        return currentType
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
                        getPingCurrentServer()
                        myCurrentIp?.postValue(
                            SessionManager(
                                motherContext.getSharedPreferences(
                                    "user_info_mother_lib", Context.MODE_PRIVATE
                                )
                            ).getLastConnServerIP()
                        )
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