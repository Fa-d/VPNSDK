package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile
import de.blinkt.openvpn.core.ConfigParser
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.IOpenVPNServiceInternal
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VPNLaunchHelper
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.Reader
import java.io.StringReader
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class OpenVpnCore @Inject constructor(private val appContext: Context) :
    VpnStatus.StateListener, VpnStatus.ByteCountListener, IVpnSpeedIP, IVpnLifecycle, IStartStop {
    private val totalUploadSpeed = MutableLiveData(0L)
    private val totalDownloadSpeed = MutableLiveData(0L)
    val currentUploadSpeed = MutableLiveData("")
    val currentDownloadSpeed = MutableLiveData("")
    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)
    private val currentIp = MutableLiveData("")


    private var mServiceOV: IOpenVPNServiceInternal? = null
    private val mConnectionOV: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mServiceOV = IOpenVPNServiceInternal.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mServiceOV = null
        }
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int, level: ConnectionStatus?, intent: Intent?
    ) {
        if (state == "DISCONNECTED") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.value = VPNStatus.DISCONNECTED
            }
        } else if (state == "CONNECTED") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.value = VPNStatus.CONNECTED
            }
        }
        else if (state == "CONNECTING" || state == "WAIT" || state == "AUTH" || state == "GET_CONFIG" || state == "ASSIGN_IP") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.value = VPNStatus.CONNECTING
            }
        }else {
        }
    }

    override fun setConnectedVPN(uuid: String?) {}

    override fun updateByteCount(totalIn: Long, totalOut: Long, diffIn: Long, diffOut: Long) {
        totalUploadSpeed.postValue(totalIn)
        totalDownloadSpeed.postValue(totalOut)
        currentUploadSpeed.postValue(humanReadableByteCount(diffIn, true))
        currentDownloadSpeed.postValue(humanReadableByteCount(diffOut, true))
    }

    override fun getUploadSpeed(): LiveData<String> {
        return currentUploadSpeed
    }

    override fun getDownloadSpeed(): LiveData<String> {
        return currentDownloadSpeed
    }

    override fun getCurrentIp(): LiveData<String> {
        return liveData { }
    }

    override fun onVPNStart() {}
    override fun onVpnCreate() {

    }

    override fun onVPNResume() {
        val intent = Intent(appContext, OpenVPNService::class.java)
        intent.action = OpenVPNService.START_SERVICE
        appContext.bindService(intent, mConnectionOV, AppCompatActivity.BIND_AUTO_CREATE)
        VpnStatus.addStateListener(this)
        VpnStatus.addByteCountListener(this)
    }

    override fun onVPNDestroy() {}

    override fun onVPNPause() {
        //  mConnectionOV?.let { motherContext.unbindService(it) }
        VpnStatus.removeStateListener(this)
        VpnStatus.removeByteCountListener(this)
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        val vpl = ProfileManager.getInstance(passedContext)
        var profile = vpl.getProfileByName("start-vpn")

        if (profile != null) {
            vpl.removeProfile(passedContext, profile)
        }

        val data: ByteArray = Base64.decode(vpnProfile.vpnConfig, Base64.DEFAULT)
        var configText = String(data, charset("UTF-8"))

        var listenPort = 0
        var serverIP: String = vpnProfile.serverIP
        var remoteServerIP = ""
        if (serverIP.contains(":")) {
            serverIP = vpnProfile.serverIP.split(":")[0]
            listenPort = Integer.valueOf(vpnProfile.serverIP.split(":")[1])
        }

        remoteServerIP = serverIP
        configText = configText.replace("remoteIP".toRegex(), serverIP)
        configText = configText.replace("remoteServerIP".toRegex(), remoteServerIP)
        configText = configText.replace("remotePort".toRegex(), "" + listenPort)

        val inputString: Reader = StringReader(configText)
        val bufferedReader = BufferedReader(inputString)
        val cp = ConfigParser()
        cp.parseConfig(bufferedReader)
        profile = cp.convertProfile()
        profile.mName = "start-vpn"
        profile.mUsername = vpnProfile.userName
        profile.mPassword = vpnProfile.password
        vpl.addProfile(profile)
        vpl.saveProfile(passedContext, profile)
        vpl.saveProfileList(passedContext)
        ProfileManager.updateLRU(passedContext.applicationContext, profile);
        VPNLaunchHelper.startOpenVpn(profile, passedContext.baseContext);
    }

    override fun stopVpn() {
        if (VpnStatus.isVPNActive()) {
            ProfileManager.setConntectedVpnProfileDisconnected(appContext)
            if (mServiceOV != null) {
                try {
                    mServiceOV?.stopVPN(false)
                    CoroutineScope(Dispatchers.Main).launch {
                        currentVpnState.value = VPNStatus.DISCONNECTED
                    }
                } catch (e: RemoteException) {
                    VpnStatus.logException(e)
                }
            }
        }
    }
}

fun humanReadableByteCount(passedBytes: Long, speed: Boolean): String {
    var bytes = passedBytes
    if (speed) bytes *= 8
    val unit: Double = if (speed) 1000.0 else 1024.0
    val exp = max(0.0, min((ln(bytes.toDouble()) / ln(unit)).toInt().toDouble(), 3.0)).toInt()
    val bytesUnit: Float = (bytes / unit.pow(exp.toDouble())).toFloat()

    return if (speed) when (exp) {
        0 -> "$bytesUnit  bit/s"
        1 -> "$bytesUnit  kbit/s"
        2 -> "$bytesUnit  Mbit/s"
        else -> "$bytesUnit  Gbit/s"
    } else when (exp) {
        0 -> "$bytesUnit  B"
        1 -> "$bytesUnit  kB"
        2 -> "$bytesUnit  MB"
        else -> "$bytesUnit  GB"
    }
}
