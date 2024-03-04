package com.faddy.vpnsdk

//import com.faddy.wgtunlib.data.model.TunnelConfig
//import com.faddy.wgtunlib.service.foreground.ServiceManager
//import com.faddy.wgtunlib.service.tile.TunnelControlTile

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.faddy.tunnelkitlib.model.VPNType
import com.faddy.tunnelkitlib.model.VpnProfile
import com.faddy.vpnsdk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity()/*, ServiceConnection.Callback */ {

    /*    @Inject
        lateinit var usedServices: UsedServices
        val serviceStatus = MutableLiveData(Status.Stopped)
        private val connection = ServiceConnection(this, this)*/
    private lateinit var binding: ActivityMainBinding
    private val coreSdk = MainApp.vpnSdk!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        initData()
        initView()
        initClickListener()
    }/*       TileService.requestListeningState(
                       applicationContext,
                       ComponentName(applicationContext, TunnelControlTile::class.java)
                       )*/

    private fun initView() {/*      serviceStatus.observe(this) { status ->
                  binding.connectionStatus.text = status.name
              }*/
    }

    override fun onPause() {
        super.onPause()
        coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        coreSdk.onVPNResume()
    }

    private fun initClickListener() {
        binding.button1.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    coreSdk.startConnect(
                        this@MainActivity, VpnProfile(
                            VPNType.OPENVPN,
                            "ss",
                            "123456",
                            vpnConfig = openVpnConf,
                            serverIP = openVpnIP
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(this@MainActivity)
            }


                // VPNController().startV2Ray(applicationContext)
                /*when (serviceStatus.value) {
                    Status.Stopped -> {
                        startService()
                    }

                    Status.Started -> {
                        BoxService.stop()
                    }

                    else -> {}

                }*/

        }
        binding.button2.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                createProfile0(topConfig)
            }/* val profile = Profile(id = 32435L, name = "asdfg", typed = TypedProfile())
             val configDirectory = File(filesDir, "configs").also { it.mkdirs() }
             Libbox.checkConfig(topConfig)
             lifecycleScope.launch(Dispatchers.IO) {
                 profile.userOrder = ProfileManager.nextOrder()
                 val fileID = ProfileManager.nextFileID()
                 val configFile = File(configDirectory, "$fileID.json")
                 ProfileManager.create(profile)
                 runCatching {
                     configFile.writeText(topConfig)
                     //File(profile.typed.path).writeText(topConfig)
                 }.onSuccess {
                     Log.e("sfdgbfh", "onSuccess")
                 }.onFailure {
                     Log.e("sfdgbfh", it.message.toString())
                 }
             }*/
            //ServiceManager.stopVpnService(applicationContext)
        }
    }

    private suspend fun createProfile0(content: String) {/*       val typedProfile = TypedProfile()
               val profile = Profile(name = "newName", typed = typedProfile)
               profile.userOrder = ProfileManager.nextOrder()
               val fileID = ProfileManager.nextFileID()
               val configDirectory = File(filesDir, "configs").also { it.mkdirs() }
               val configFile = File(configDirectory, "$fileID.json")
               typedProfile.path = configFile.path
               typedProfile.type = TypedProfile.Type.Local
               Libbox.checkConfig(content)
               configFile.writeText(content)
               ProfileManager.create(profile)*/
    }

    private fun initData() {
        // Use mmkv here
        coreSdk.getConnectedTime()
        coreSdk.getVpnConnectedStatus().observe(this) { status ->
            status
        }

    }

    /* override fun onServiceStatusChanged(status: Status) {
        serviceStatus.postValue(status)
    }

   ServiceManager.startVpnService(
                    this@MainActivity, TunnelConfig(
                        id = 0,
                        name = "newName",
                        wgQuick = "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = YOUR_PRIVATE_KEY_HERE\n\n[Peer]\nPublicKey = YOUR_PUBLIC_KEY_HERE\nPresharedKey = YOUR_PRESHARED_KEY_HERE\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 10.0.0.2:51820\nPersistentKeepalive = 25"
                    ).toString()
                )*/

    override fun onDestroy() {
        //  connection.disconnect()
        coreSdk.onVPNDestroy()
        super.onDestroy()
    }

    fun reconnect() {
        //   connection.reconnect()
    }

    private val prepareLauncher = registerForActivityResult(PrepareService()) {
        if (it) {
            startService()
        } else {
            //  onServiceAlert(Alert.RequestVPNPermission, null)
        }
    }

    private class PrepareService : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

    private suspend fun prepare() = withContext(Dispatchers.Main) {
        try {
            val intent = VpnService.prepare(this@MainActivity)
            if (intent != null) {
                prepareLauncher.launch(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            //  onServiceAlert(Alert.RequestVPNPermission, e.message)
            false
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            startService()
        } else {
            //   onServiceAlert(Alert.RequestNotificationPermission, null)
        }
    }

    fun startService() {/*if (!usedServices.notificatip.areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            return
        }*/

        /*lifecycleScope.launch(Dispatchers.IO) {
            if (Settings.rebuildServiceMode()) {
                reconnect()
            }
            if (Settings.serviceMode == ServiceMode.VPN) {
                if (prepare()) return@launch
            }
            val intent = Intent(this@MainActivity, Settings.serviceClass())
            withContext(Dispatchers.Main) {
                ContextCompat.startForegroundService(this@MainActivity, intent)
            }
        }*/
    }
}

val openVpnConf =
    "Y2xpZW50CnByb3RvIHVkcApleHBsaWNpdC1leGl0LW5vdGlmeQpyZW1vdGUgMTAuMC4wLjEgMTE5NApkZXYgdHVuCnJlc29sdi1yZXRyeSBpbmZpbml0ZQpub2JpbmQKcGVyc2lzdC1rZXkKcGVyc2lzdC10dW4KcmVtb3RlLWNlcnQtdGxzIHNlcnZlcgp2ZXJpZnkteDUwOS1uYW1lIHNlcnZlcl9FWEFNUExFX1ZFUklGWV9OQU1FIG5hbWUKYXV0aCBTSEEyNTYKYXV0aC1ub2NhY2hlCmF1dGgtdXNlci1wYXNzCmRoY3Atb3B0aW9uIEROUyA4LjguOC44CmRoY3Atb3B0aW9uIERPTUFJTiBnb29nbGUuY29tCmNpcGhlciAgQUVTLTEyOC1HQ00KdGxzLWNsaWVudAp0bHMtdmVyc2lvbi1taW4gMS4yCnRscy1jaXBoZXIgVExTLUVDREhFLUVDRFNBLVdJVEgtQUVTLTEyOC1HQ00tU0hBMjU2Cmlnbm9yZS11bmtub3duLW9wdGlvbiBibG9jay1vdXRzaWRlLWRucwpzZXRlbnYgb3B0IGJsb2NrLW91dHNpZGUtZG5zICMgUHJldmVudCBXaW5kb3dzIDEwIEROUyBsZWFrCnNldGVudiBDTElFTlRfQ0VSVCAwCnZlcmIgMwo8Y2E+Ci0tLS0tQkVHSU4gQ0VSVElGSUNBVEUtLS0tLQpZT1VSX0NBX0NFUlRJRklDQVRFX0hFUkUKLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo8L2NhPgo8dGxzLWNyeXB0PgojCiMgMjA0OCBiaXQgT3BlblZQTiBzdGF0aWMga2V5CiMKLS0tLS1CRUdJTiBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQpZT1VSX09QRU5WUE5fU1RBVElDX0tFWV9IRVJFCi0tLS0tRU5EIE9wZW5WUE4gU3RhdGljIGtleSBWMS0tLS0tCjwvdGxzLWNyeXB0Pgo="
val openVpnIP = "10.0.0.1:1194"

val topConfig = """{
  "log": {
    "disabled": false,
    "level": "info",
    "timestamp": true
  },
  "experimental": {
    "clash_api": {
      "external_controller": "127.0.0.1:9090",
      "external_ui": "ui",
      "external_ui_download_url": "",
      "external_ui_download_detour": "",
      "secret": "",
      "default_mode": "Rule"
    },
    "cache_file": {
      "enabled": true,
      "path": "cache.db",
      "store_fakeip": true
    }
  },
  "dns": {
    "servers": [
      {
        "tag": "proxydns",
        "address": "https://8.8.8.8/dns-query",
        "detour": "select"
      },
      {
        "tag": "localdns",
        "address": "h3://223.5.5.5/dns-query",
        "detour": "direct"
      },
      {
        "address": "rcode://refused",
        "tag": "block"
      },
      {
        "tag": "dns_fakeip",
        "address": "fakeip"
      }
    ],
    "rules": [
      {
        "outbound": "any",
        "server": "localdns",
        "disable_cache": true
      },
      {
        "clash_mode": "Global",
        "server": "proxydns"
      },
      {
        "clash_mode": "Direct",
        "server": "localdns"
      }
    ],
    "fakeip": {
      "enabled": true,
      "inet4_range": "198.18.0.0/15",
      "inet6_range": "fc00::/18"
    },
    "independent_cache": true,
    "final": "proxydns"
  },
  "inbounds": [
    {
      "type": "tun",
      "inet4_address": "172.19.0.1/30",
      "inet6_address": "fd00::1/126",
      "auto_route": true,
      "strict_route": true,
      "sniff": true,
      "sniff_override_destination": true,
      "domain_strategy": "prefer_ipv4"
    }
  ],
  "outbounds": [
    {
      "tag": "select",
      "type": "selector",
      "default": "vmess-vps",
      "outbounds": [
        "vmess-vps"
      ]
    },
    {
      "server": "10.0.0.4",
      "server_port": 2095,
      "tag": "vmess-vps",
      "tls": {
        "enabled": false,
        "server_name": "https://www.bing.com",
        "insecure": false,
        "utls": {
          "enabled": true,
          "fingerprint": "chrome"
        }
      },
      "transport": {
        "headers": {
          "Host": [
            "https://www.bing.com"
          ]
        },
        "path": "53ada59c-7166-48fc-8b0d-de5a135381b8-vm",
        "type": "ws"
      },
      "type": "vmess",
      "security": "auto",
      "uuid": "53ada59c-7166-48fc-8b0d-de5a135381b8"
    },
    {
      "tag": "direct",
      "type": "direct"
    },
    {
      "tag": "block",
      "type": "block"
    },
    {
      "tag": "dns-out",
      "type": "dns"
    }
  ],
  "route": {
    "auto_detect_interface": true,
    "final": "select",
    "rules": [
      {
        "outbound": "dns-out",
        "protocol": "dns"
      },
      {
        "clash_mode": "Direct",
        "outbound": "direct"
      },
      {
        "clash_mode": "Global",
        "outbound": "select"
      },
      {
        "ip_is_private": true,
        "outbound": "direct"
      }
    ]
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







