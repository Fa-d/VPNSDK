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
import com.faddy.motherlib.vpnCores.VpnSwitchFactory

@SuppressLint("StaticFieldLeak")
object MotherVPN : ICoreVpn, IVpnStatus, IVpnLifecycle, IVpnSpeedIP {
    private lateinit var motherContext: Context
    private val connectedVpnTime = MutableLiveData("00:00:00")
    var connectedStatus = MutableLiveData(VPNStatus.DISCONNECTED)
    private val vpnSwitchFactory = VpnSwitchFactory()
    fun init(passedContext: Context): MotherVPN {
        motherContext = passedContext
        return this
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
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun disconnect(): LiveData<VPNStatus> {
        vpnSwitchFactory.stopVpn(getLastSelectedVpn(), motherContext)
        return vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn())
    }

    override fun getVpnConnectedStatus() {
        connectedStatus.postValue(vpnSwitchFactory.setVpnStateListeners(getLastSelectedVpn()).value)
    }

    override fun getVpnType(): LiveData<VPNType> {
        return liveData { getLastSelectedVpn() }
    }

    override fun setVpnType(vpnType: VPNType) {
        SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).setLastConnVpnType(vpnType.name)
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

    override fun getCurrentIp(): LiveData<String> {
        return vpnSwitchFactory.getCurrentIp(getLastSelectedVpn())
    }

    private fun getLastSelectedVpn(): VPNType {
        val vpnState = SessionManager(
            motherContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
        ).getLastConnVpnType() ?: ""
        return when (vpnState) {
            "OPENVPN" -> VPNType.OPENVPN
            "OPENCONNECT" -> VPNType.OPENCONNECT
            "WIREGUARD" -> VPNType.WIREGUARD
            "IPSECIKEV2" -> VPNType.IPSECIKEV2
            "SINGBOX" -> VPNType.SINGBOX
            else -> VPNType.NONE
        }
    }
}