package com.faddy.motherlib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.VpnService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.faddy.motherlib.interfaces.ICoreVpn
import com.faddy.motherlib.interfaces.IVpnLifecycle
import com.faddy.motherlib.interfaces.IVpnSpeedIP
import com.faddy.motherlib.interfaces.IVpnStatus
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.utils.SessionManager
import com.faddy.motherlib.utils.toMutableLiveData
import com.faddy.motherlib.vpnCores.VpnSwitchFactory

@SuppressLint("StaticFieldLeak")
object MotherVPN : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {
    private lateinit var motherContext: Context
    private val vpnSwitchFactory = VpnSwitchFactory()

    var connectedVpnTime = MutableLiveData("00:00:00")
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

        /*if (isVpnConnected()) {
            currentUploadSpeed = vpnSwitchFactory.getDownloadSpeed(getLastSelectedVpn())
            currentDownloadSpeed = vpnSwitchFactory.getUploadSpeed(getLastSelectedVpn())
            connectedStatus = vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
            myCurrentIp = vpnSwitchFactory.getCurrentIp(getLastSelectedVpn())
        } else {
            currentUploadSpeed = liveData { 0L }
            currentDownloadSpeed = liveData { 0L }
            myCurrentIp = liveData { "0.0.0.0" }
        }*/
    }
    override fun startConnect(
        passedActivity: Activity, vpnProfile: VpnProfile
    ): LiveData<VPNStatus> {
        setVpnType(vpnProfile.vpnType)
        getVpnConnectedStatus()
        if (!isVpnConnected()) {
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
        currentUploadSpeed?.postValue(0L)
        currentDownloadSpeed?.postValue(0L)
        connectedStatus?.postValue(VPNStatus.DISCONNECTED)
        myCurrentIp?.postValue("0.0.0.0")
        funInvoker?.invoke()
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun setVpnType(vpnType: VPNType) {
        SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).setLastConnVpnType(vpnType.name)
    }
    override fun getConnectedTime(): LiveData<String> {
        return if (isVpnConnected()) connectedVpnTime else liveData { "00:00:00" }
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
        funInvoker?.invoke()
    }

    override fun onVPNDestroy() {
        vpnSwitchFactory.onVPNDestroy(getLastSelectedVpn())
    }

    override fun onVPNPause() {
        vpnSwitchFactory.onVPNPause(getLastSelectedVpn())
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
}