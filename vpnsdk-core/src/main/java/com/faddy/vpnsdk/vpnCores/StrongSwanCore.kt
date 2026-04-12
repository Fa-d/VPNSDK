package com.faddy.vpnsdk.vpnCores

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.faddy.vpnsdk.interfaces.IStartStop
import com.faddy.vpnsdk.interfaces.IVpnLifecycle
import com.faddy.vpnsdk.interfaces.IVpnSpeedIP
import com.faddy.vpnsdk.model.VPNStatus
import com.faddy.vpnsdk.model.VpnProfile
import org.strongswan.android.Controller
import org.strongswan.android.data.VpnType
import org.strongswan.android.logic.VpnStateService
import javax.inject.Inject

class StrongSwanCore @Inject constructor(private val appContext: Context) :
    IVpnSpeedIP, IVpnLifecycle, IStartStop {

    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)
    private val currentUploadSpeed = MutableLiveData(0L)
    private val currentDownloadSpeed = MutableLiveData(0L)

    private var mService: VpnStateService? = null
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = (service as VpnStateService.LocalBinder).service
            mService?.registerListener(stateListener)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }
    }

    private val stateListener = VpnStateService.VpnStateListener {
        val service = mService ?: return@VpnStateListener
        when (service.state) {
            VpnStateService.State.CONNECTING -> currentVpnState.postValue(VPNStatus.CONNECTING)
            VpnStateService.State.CONNECTED -> currentVpnState.postValue(VPNStatus.CONNECTED)
            VpnStateService.State.DISCONNECTING -> currentVpnState.postValue(VPNStatus.DISCONNECTING)
            VpnStateService.State.DISABLED -> currentVpnState.postValue(VPNStatus.DISCONNECTED)
        }
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        val profile = org.strongswan.android.data.VpnProfile()
        profile.name = "vpnsdk-ipsec"
        profile.gateway = vpnProfile.serverIP.split(":")[0]
        profile.username = vpnProfile.userName
        profile.password = vpnProfile.password
        profile.vpnType = VpnType.IKEV2_EAP

        Controller.init(appContext, Intent())
        Controller.insertProfile(profile, appContext)
        Controller.startVpn(passedContext, profile)
    }

    override fun stopVpn() {
        Controller.stopVpn()
        currentVpnState.postValue(VPNStatus.DISCONNECTED)
    }

    override fun onVPNStart() {}

    override fun onVpnCreate() {
        appContext.bindService(
            Intent(appContext, VpnStateService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onVPNResume() {
        mService?.registerListener(stateListener)
    }

    override fun onVPNDestroy() {
        mService?.unregisterListener(stateListener)
        try {
            appContext.unbindService(mServiceConnection)
        } catch (_: IllegalArgumentException) {}
    }

    override fun onVPNPause() {
        mService?.unregisterListener(stateListener)
    }

    override fun getUploadSpeed(): LiveData<Long> = currentUploadSpeed

    override fun getDownloadSpeed(): LiveData<Long> = currentDownloadSpeed

    override fun getCurrentIp(): LiveData<String> = liveData { }
}
