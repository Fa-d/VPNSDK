package com.faddy.singbox

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.faddy.singbox.bg.BoxService
import com.faddy.singbox.bg.ServiceConnection
import com.faddy.singbox.bg.ServiceNotification
import com.faddy.singbox.constant.Alert
import com.faddy.singbox.constant.ServiceMode
import com.faddy.singbox.constant.Status
import com.faddy.singbox.database.Profile
import com.faddy.singbox.database.ProfileManager
import com.faddy.singbox.database.Settings
import com.faddy.singbox.database.TypedProfile
import com.faddy.singbox.utils.CommandClient
import io.nekohasekai.libbox.Libbox
import io.nekohasekai.libbox.StatusMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SingBoxCore(val passedContext: Context, passedActivity: AppCompatActivity) :
    ServiceConnection.Callback {

    val serviceStatus = MutableLiveData(Status.Stopped)
    val uploadDiff = MutableLiveData("0L")
    val downloadDiff = MutableLiveData("0L")
    private val connection = ServiceConnection(passedActivity, this)
    private val statusClient = CommandClient(
        CoroutineScope(Dispatchers.IO), CommandClient.ConnectionType.Status, StatusClient()
    )

    override fun onServiceStatusChanged(status: Status) {
        serviceStatus.postValue(status)
    }

    inner class StatusClient : CommandClient.Handler {

        override fun onConnected() {
            CoroutineScope(Dispatchers.Main).launch {

            }
        }

        override fun onDisconnected() {
            CoroutineScope(Dispatchers.Main).launch {

            }
        }

        override fun updateStatus(status: StatusMessage) {

            CoroutineScope(Dispatchers.Main).launch {
                Libbox.formatBytes(status.memory)
                status.goroutines.toString()
                val trafficAvailable = status.trafficAvailable
                if (trafficAvailable) {
                    status.connectionsIn.toString()
                    status.connectionsOut.toString()
                    uploadDiff.postValue(Libbox.formatBytes(status.uplink) + "/s")
                    downloadDiff.postValue(Libbox.formatBytes(status.downlink) + "/s")
                    Libbox.formatBytes(status.uplinkTotal)
                    Libbox.formatBytes(status.downlinkTotal)
                }
            }
        }

    }

    private suspend fun createProfile0(content: String) {
        val typedProfile = TypedProfile()
        val profile = Profile(id = 1L, name = "newName", typed = typedProfile)
        profile.userOrder = ProfileManager.nextOrder()
        val fileID = ProfileManager.nextFileID()
        val configDirectory =
            File("/data/data/com.faddy.vpnsdk/files/", "configs").also { it.mkdirs() }
        val configFile = File(configDirectory, "1.json")
        typedProfile.path = configFile.path
        typedProfile.type = TypedProfile.Type.Local
        Libbox.checkConfig(content)
        configFile.writeText(content)
        ProfileManager.create(profile)
    }

    private suspend fun prepare() = withContext(Dispatchers.Main) {
        try {
            val intent = VpnService.prepare(passedContext)
            if (intent != null) {
                prepareLauncher.launch(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            onServiceAlert(Alert.RequestVPNPermission, e.message)
            false
        }
    }

    fun startConnect() {
        when (serviceStatus.value) {
            Status.Stopped -> {
                CoroutineScope(Dispatchers.IO).launch {
                    prepare()
                    createProfile0(topConfig)
                }.invokeOnCompletion {
                    startService()
                }
            }

            Status.Started -> {
                BoxService.stop()
            }

            else -> {}
        }
    }

    private val prepareLauncher = passedActivity.registerForActivityResult(PrepareService()) {
        if (it) {
            startService()
        } else {
            onServiceAlert(Alert.RequestVPNPermission, null)
        }
    }

    private class PrepareService : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == AppCompatActivity.RESULT_OK
        }
    }

    private val notificationPermissionLauncher = passedActivity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            startService()
        } else {
            onServiceAlert(Alert.RequestNotificationPermission, null)
        }
    }

    fun reconnect(lifecycleOwner: LifecycleOwner? = null) {
        connection.reconnect()
        if (lifecycleOwner != null) {
            serviceStatus.observe(lifecycleOwner) {
                when (it) {
                    Status.Stopped -> {
                    }

                    Status.Starting -> {
                    }

                    Status.Started -> {
                        statusClient.connect()
                    }

                    Status.Stopping -> {
                    }

                    else -> {}
                }
            }
        }

    }

    fun startService() {
        if (!ServiceNotification.checkPermission()) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            if (Settings.rebuildServiceMode()) {
                reconnect()
            }
            if (Settings.serviceMode == ServiceMode.VPN) {
                if (prepare()) return@launch
            }
            val intent = Intent(passedContext, Settings.serviceClass())
            withContext(Dispatchers.Main) {
                ContextCompat.startForegroundService(passedContext, intent)
            }
        }
    }

    fun onDestroy() {
        connection.disconnect()
        statusClient.disconnect()
    }

    val topConfig = """{
    "log": {
        "disabled": false,
        "level": "warn",
        "timestamp": true
    },
    "dns": {
        "servers": [
            {
                "tag": "dns_block",
                "address": "rcode://success"
            },
            {
                "tag": "dns_fakeip",
                "address": "fakeip"
            },
            {
                "tag": "dns_local",
                "address": "223.5.5.5",
                "detour": "direct"
            }
        ],
        "rules": [
            {
                "outbound": "any",
                "server": "dns_local"
            },
            {
                "geosite": [
                    "category-ads-all"
                ],
                "server": "dns_block",
                "disable_cache": true
            },
            {
                "query_type": [
                    "A",
                    "AAAA"
                ],
                "server": "dns_fakeip"
            }
        ],
        "strategy": "ipv4_only",
        "independent_cache": true,
        "fakeip": {
            "enabled": true,
            "inet4_range": "198.18.0.0/15",
            "inet6_range": "fc00::/18"
        }
    },
    "route": {
        "rules": [
            {
                "protocol": "dns",
                "outbound": "dns-out"
            },
            {
                "geoip": "private",
                "outbound": "direct"
            },
            {
                "clash_mode": "Direct",
                "outbound": "direct"
            },
            {
                "clash_mode": "Global",
                "outbound": "select"
            }
        ],
        "final": "select",
        "auto_detect_interface": true
    },
    "inbounds": [
        {
            "type": "tun",
            "tag": "tun-in",
            "inet4_address": "172.19.0.1/30",
            "inet6_address": "fdfe:dcba:9876::1/126",
            "auto_route": true,
            "strict_route": true,
            "stack": "mixed",
            "sniff": true,
            "sniff_override_destination": false
        }
    ],
    "outbounds": [
        {
            "type": "vmess",
            "tag": "vmess-vps",
            "server": "45.137.150.214",
            "server_port": 1111,
            "uuid": "adf45974-6e9e-4954-87fb-af6443d7384a",
            "security": "auto",
            "alter_id": 0,
            "packet_encoding": "xudp",
            "multiplex": {
                "enabled": true,
                "protocol": "h2mux",
                "max_connections": 1,
                "min_streams": 4,
                "padding": false
            }
        },
        {
            "type": "selector",
            "tag": "select",
            "outbounds": [
                "vmess-vps"
            ],
            "default": "vmess-vps",
            "interrupt_exist_connections": false
        },
        {
            "type": "direct",
            "tag": "direct"
        },
        {
            "type": "block",
            "tag": "block"
        },
        {
            "type": "dns",
            "tag": "dns-out"
        }
    ],
    "experimental": {
        "cache_file": {
            "enabled": true
        }
    },
    "ntp": {
        "enabled": true,
        "server": "time.apple.com",
        "server_port": 123,
        "interval": "30m",
        "detour": "direct"
    }
}
"""


}