package com.faddy.motherlib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faddy.motherlib.interfaces.ICoreVpn
import com.faddy.motherlib.interfaces.IVpnLifecycle
import com.faddy.motherlib.interfaces.IVpnSpeedIP
import com.faddy.motherlib.interfaces.IVpnStatus
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.utils.VPNStatus
import com.faddy.motherlib.utils.VPNType
import de.blinkt.openconnect.core.OpenConnectManagementThread
import de.blinkt.openconnect.core.VPNConnector
import de.blinkt.openconnect.core.VpnStatusOC
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.IOpenVPNServiceInternal
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.VpnStatusOV
import de.blinkt.service.OpenConnectService

@SuppressLint("StaticFieldLeak")
object MotherVPN : VpnStatusOV.StateListener, VpnStatusOC.ByteCountListener, ICoreVpn,
    IVpnLifecycle, IVpnSpeedIP, IVpnStatus {
    private lateinit var motherContext: Context
    private val connectedStatus = MutableLiveData(VPNStatus.DISCONNECTED)
    private val currentSelectedVpnType = MutableLiveData(VPNType.NONE)
    private val connectedVpnTime = MutableLiveData("")
    private val currentUploadSpeed = MutableLiveData(0L)
    private val totalUploadSpeed = MutableLiveData(0L)
    private val currentDownloadSpeed = MutableLiveData(0L)
    private val totalDownloadSpeed = MutableLiveData(0L)
    private val currentIp = MutableLiveData("")
    private var mConn: VPNConnector? = null
    private var mServiceOV: IOpenVPNServiceInternal? = null
    private val mConnectionOV: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mServiceOV = IOpenVPNServiceInternal.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mServiceOV = null
        }
    }
    private var mServiceOC: IOpenVPNServiceInternal? = null
    private val mConnectionOC: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mServiceOC = IOpenVPNServiceInternal.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mServiceOC = null
        }
    }

    fun init(passedContext: Context): MotherVPN {
        motherContext = passedContext
        return this
    }

    override fun startConnect(type: VPNType, vpnProfile: VpnProfile): LiveData<VPNStatus> {
        if (!isVpnConnected()) {
            when (type) {
                VPNType.NONE -> {

                }

                VPNType.OPENVPN -> {

                }

                VPNType.OPENCONNECT -> {

                }

                VPNType.WIREGUARD -> {

                }

                VPNType.IPSECIKEV2 -> {

                }

                VPNType.SINGBOX -> {

                }
            }
        } else {
            disconnect()
        }
        return connectedStatus
    }

    override fun disconnect(): LiveData<VPNStatus> {
        return connectedStatus
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return currentUploadSpeed
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return currentDownloadSpeed
    }

    override fun getCurrentIp(): LiveData<String> {
        return currentIp
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return connectedStatus
    }

    override fun getVpnType(): LiveData<VPNType> {
        return currentSelectedVpnType
    }

    override fun getConnectedTime(): LiveData<String> {
        return connectedVpnTime
    }

    override fun isVpnConnected(): Boolean {
        return connectedStatus.value == VPNStatus.CONNECTED
    }

    override fun setVpnStatus(vpnState: VPNStatus) {
        connectedStatus.postValue(vpnState)
    }

    override fun isVpnServicePrepared(): Boolean {
        return VpnService.prepare(motherContext) == null
    }

    override fun prepareVPNService(passedActivity: Activity) {
        val vpnIntent = VpnService.prepare(passedActivity)
        if (vpnIntent != null) {
            passedActivity.startActivityForResult(vpnIntent, 1000)
        }
    }

    override fun onVPNStart() {

    }

    override fun onVPNResume() {
        val intent = Intent(motherContext, OpenVPNService::class.java)
        intent.action = OpenVPNService.START_SERVICE
        motherContext.bindService(intent, mConnectionOV, AppCompatActivity.BIND_AUTO_CREATE)
        VpnStatusOV.addStateListener(this)
        VpnStatusOC.addByteCountListener(this)
        mConn = object : VPNConnector(motherContext, true) {
            override fun onUpdate(service: OpenConnectService?) {
                if (mConn!!.service.connectionState == OpenConnectManagementThread.STATE_CONNECTED) {
                    setVpnStatus(VPNStatus.CONNECTED)
                }
            }
        }
    }

    override fun onVPNDestroy() {}

    override fun onVPNPause() {}

    override fun updateByteCount(totalIn: Long, totalOut: Long, diffIn: Long, diffOut: Long) {
        totalUploadSpeed.postValue(totalIn)
        totalDownloadSpeed.postValue(totalOut)
        currentUploadSpeed.postValue(diffIn)
        currentDownloadSpeed.postValue(diffOut)
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
    }

    override fun setConnectedVPN(uuid: String?) {}
}