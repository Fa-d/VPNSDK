package de.blinkt

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.phoenixLib.commoncore.interfaces.IStartStop
import com.phoenixLib.commoncore.interfaces.IVpnLifecycle
import com.phoenixLib.commoncore.interfaces.IVpnSpeedIP
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VPNType
import com.phoenixLib.commoncore.model.VpnProfile
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.core.ConfigParser
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.IOpenVPNServiceInternal
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.Reader
import java.io.StringReader
import javax.inject.Inject

class OpenVpnCore @Inject constructor(
    private val appContext: Context, private val localBroadcastManager: LocalBroadcastManager
) : VpnStatus.StateListener, VpnStatus.ByteCountListener, IVpnSpeedIP, IVpnLifecycle, IStartStop {
    private var totalUploadSpeed = MutableStateFlow(0L)
    private var totalDownloadSpeed = MutableStateFlow(0L)
    private var currentUploadSpeed = MutableStateFlow(0L)
    private var currentDownloadSpeed = MutableStateFlow(0L)
    private var currentVpnState = MutableStateFlow(VPNStatus.DISCONNECTED)
    private var currentIp = MutableStateFlow("")

    private var mServiceOV: IOpenVPNServiceInternal? = null
    private val mConnectionOV: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mServiceOV = IOpenVPNServiceInternal.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mServiceOV = null
        }
    }


    private val conDisReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == "conDisReceiver") {
                if (intent.getStringExtra("conDisReceiver") == "startConnect") {
                    startVpn(
                        VpnConfigs.getVPNProfile(VPNType.OPENVPN),
                        Class.forName("com.faddy.vpnsdk.MainActivityPro") as Activity,
                    )
                } else if (intent.getStringExtra("conDisReceiver") == "stopVpn") {
                    stopVpn();
                }
            }
        }
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        intent: Intent?
    ) {
        if (state == "DISCONNECTED") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.emit(VPNStatus.DISCONNECTED)
            }
        } else if (state == "CONNECTED") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.emit(VPNStatus.CONNECTED)
            }
        } else if (state == "CONNECTING" || state == "WAIT" || state == "AUTH" || state == "GET_CONFIG" || state == "ASSIGN_IP") {
            CoroutineScope(Dispatchers.Main).launch {
                currentVpnState.emit(VPNStatus.CONNECTING)
            }
        }
        val conDisIntent = Intent("conState")
        conDisIntent.putExtra("conState", currentVpnState.value.toString())
        localBroadcastManager.sendBroadcast(conDisIntent)
        Log.e("OpenVpnCore", currentVpnState.value.toString())
    }

    override fun setConnectedVPN(uuid: String?) {}

    override fun updateByteCount(totalIn: Long, totalOut: Long, diffIn: Long, diffOut: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            totalUploadSpeed.emit(totalIn)
            totalDownloadSpeed.emit(totalOut)
            currentUploadSpeed.emit(diffIn)
            currentDownloadSpeed.emit(diffOut)
        }
    }

    fun getVpnState(): Flow<VPNStatus> {
        return currentVpnState
    }

    override fun getUploadSpeed(): Flow<Long> {
        return currentUploadSpeed
    }

    override fun getDownloadSpeed(): Flow<Long> {
        return currentDownloadSpeed
    }

    override fun getCurrentIp(): Flow<String> {
        return currentIp
    }

    override fun onVPNStart() {}

    override fun onVPNResume(activity: Activity) {
        localBroadcastManager.registerReceiver(conDisReceiver, IntentFilter("conDisReceiver"))
        val intent = Intent(appContext, OpenVPNService::class.java)
        intent.action = OpenVPNService.START_SERVICE
        appContext.bindService(intent, mConnectionOV, AppCompatActivity.BIND_AUTO_CREATE)
        VpnStatus.addStateListener(this)
        VpnStatus.addByteCountListener(this)
    }

    override fun onVPNDestroy() {
        localBroadcastManager.unregisterReceiver(conDisReceiver)
    }

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

        val intent = Intent(passedContext, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.setAction(Intent.ACTION_MAIN)
        passedContext.startActivity(intent)
    }

    override fun stopVpn() {
        if (VpnStatus.isVPNActive()) {
            ProfileManager.setConntectedVpnProfileDisconnected(appContext)
            if (mServiceOV != null) {
                try {
                    mServiceOV?.stopVPN(false)
                    CoroutineScope(Dispatchers.Main).launch {
                        currentVpnState.emit(VPNStatus.DISCONNECTED)
                    }
                } catch (e: RemoteException) {
                    VpnStatus.logException(e)
                }
            }
        }
    }
}