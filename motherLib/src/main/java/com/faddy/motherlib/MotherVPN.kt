package com.faddy.motherlib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.VpnService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faddy.motherlib.interfaces.ICoreVpn
import com.faddy.motherlib.interfaces.IVpnLifecycle
import com.faddy.motherlib.interfaces.IVpnStatus
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile
import com.faddy.motherlib.vpnCores.VpnSwitchFactory

@SuppressLint("StaticFieldLeak")
object MotherVPN : ICoreVpn, IVpnStatus, IVpnLifecycle {
    private lateinit var motherContext: Context
    private val connectedStatus = MutableLiveData(VPNStatus.DISCONNECTED)
    private val currentSelectedVpnType = MutableLiveData(VPNType.NONE)
    private val connectedVpnTime = MutableLiveData("")
    private val vpnSwitchFactory = VpnSwitchFactory()
    fun init(passedContext: Context): MotherVPN {
        motherContext = passedContext
        return this
    }

    override fun startConnect(
        passedActivity: Activity, vpnProfile: VpnProfile
    ): LiveData<VPNStatus> {
        setVpnType(vpnProfile.vpnType)
        if (!isVpnConnected()) {
            vpnSwitchFactory.startVpn(vpnProfile, motherContext, passedActivity)
        } else {
            disconnect()
        }
        return connectedStatus
    }

    override fun disconnect(): LiveData<VPNStatus> {
        vpnSwitchFactory.stopVpn()
        return connectedStatus
    }

    override fun getVpnConnectedStatus(): LiveData<VPNStatus> {
        return connectedStatus
    }

    override fun getVpnType(): LiveData<VPNType> {
        return currentSelectedVpnType
    }

    override fun setVpnType(vpnType: VPNType) {
        currentSelectedVpnType.postValue(vpnType)
    }

    override fun setVpnListeners() {
        if (currentSelectedVpnType.value == VPNType.OPENVPN) {
            //   OpenVpnCore(motherContext)
        }
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

    override fun prepareVPNService(context: Activity) {
        val vpnIntent = VpnService.prepare(context)
        //  vpnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (vpnIntent != null) {
            context.startActivityForResult(vpnIntent, 1000)
        }
    }

    override fun onVPNStart() {
        vpnSwitchFactory.onVPNStart(currentSelectedVpnType.value ?: VPNType.NONE)
    }

    override fun onVPNResume() {
        vpnSwitchFactory.onVPNResume(currentSelectedVpnType.value ?: VPNType.NONE)
    }

    override fun onVPNDestroy() {
        vpnSwitchFactory.onVPNDestroy(currentSelectedVpnType.value ?: VPNType.NONE)
    }

    override fun onVPNPause() {
        vpnSwitchFactory.onVPNPause(currentSelectedVpnType.value ?: VPNType.NONE)
    }
}