package com.faddy.motherlib

import android.content.Intent
import androidx.lifecycle.LiveData
import com.faddy.motherlib.utils.VPNStatus
import com.faddy.motherlib.utils.VPNType
import de.blinkt.openconnect.core.VpnStatusOC
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.VpnStatusOV

class MotherVPN() : VpnStatusOV.StateListener, VpnStatusOC.ByteCountListener {
    override fun updateByteCount(inSpeed: Long, outSpeed: Long, diffIn: Long, diffOut: Long) {

    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        intent: Intent?
    ) {

    }

    override fun setConnectedVPN(uuid: String?) {

    }

}

interface CoreVPNFeatures {
    fun startConnect(type: VPNType): LiveData<VPNStatus>
    fun disconnect(): LiveData<VPNStatus>
    fun getUploadSpeed(): LiveData<Long>
    fun getDownloadSpeed(): LiveData<Long>
    fun getCurrentIp(): LiveData<String>
    fun getVpnStatus(): LiveData<VPNStatus>
    fun getVpnType(): LiveData<VPNType>
    fun getConnectedTime(): LiveData<String>
}