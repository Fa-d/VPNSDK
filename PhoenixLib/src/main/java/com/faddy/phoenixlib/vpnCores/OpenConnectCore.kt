package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.openconnect.api.GrantPermissionsActivity
import com.app.openconnect.core.OpenConnectManagementThread
import com.app.openconnect.core.OpenVpnService
import com.app.openconnect.core.ProfileManager
import com.app.openconnect.core.VPNConnector
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class OpenConnectCore @Inject constructor(val context: Context) : IVpnSpeedIP, IVpnLifecycle,
    IStartStop {
    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)

    private var mConn: VPNConnector? = null

    init {
        ProfileManager.init(context)
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return liveData { }
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return liveData { }
    }

    override fun getCurrentIp(): LiveData<String> {
        return liveData { }
    }

    override fun onVPNStart() {

    }

    override fun onVpnCreate() {
    }

    override fun onVPNResume() {
        mConn = object : VPNConnector(context, false) {
            override fun onUpdate(service: OpenVpnService?) {

                CoroutineScope(Dispatchers.Main).launch {
                    val state = service?.connectionState
                    if (state == OpenConnectManagementThread.STATE_CONNECTED) {
                        currentVpnState.value = VPNStatus.CONNECTED
                    } else if (state == OpenConnectManagementThread.STATE_DISCONNECTED) {
                        currentVpnState.value = VPNStatus.DISCONNECTED
                    } else if (state == OpenConnectManagementThread.STATE_DISCONNECTED) {
                        currentVpnState.value = VPNStatus.CONNECTING
                    }
                }
            }
        }
    }

    override fun onVPNDestroy() {

    }

    override fun onVPNPause() {
        mConn?.stopActiveDialog()
        mConn?.unbind()
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        val profile = ProfileManager.create("1.1.1.1")
        val intent = Intent(passedContext, GrantPermissionsActivity::class.java)
        val pkg: String = passedContext.applicationContext.packageName
        intent.putExtra(pkg + GrantPermissionsActivity.EXTRA_UUID, profile.uuid.toString())
        intent.setAction(Intent.ACTION_MAIN)
        passedContext.startActivity(intent)
    }

    override fun stopVpn() {

    }
}