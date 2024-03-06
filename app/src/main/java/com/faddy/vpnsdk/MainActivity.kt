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
        initObserver()
    }

    private fun initObserver() {
        /*    coreSdk.setVpnStateListeners().observe(this) { state ->
                Log.e("setVpnStateListeners", state.toString())
                when (state) {
                    VPNStatus.DISCONNECTING -> {
                        binding.connectionStatus.text = "DISCONNECTING"
                    }

                    VPNStatus.CONNECTED -> {
                        binding.connectionStatus.text = "CONNECTED"
                    }

                    VPNStatus.CONNECTING -> {
                        binding.connectionStatus.text = "CONNECTING"
                    }

                    VPNStatus.DISCONNECTED -> {
                        binding.connectionStatus.text = "DISCONNECTED"
                    }
                }
            }*/
    }

    private fun initView() {/*      serviceStatus.observe(this) { status ->
                  binding.connectionStatus.text = status.name
              }*/

        coreSdk.getCurrentIp().observe(this) { text ->
            binding.ipText.text = text
        }
        coreSdk.getUploadSpeed().observe(this) { text ->
            binding.uploadText.text = text.toString()
        }
        coreSdk.getDownloadSpeed().observe(this) { text ->
            binding.downloadText.text = text.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        coreSdk.onVPNResume(this@MainActivity)
    }

    private fun initClickListener() {
        binding.button1.setOnClickListener {
            /*      if (coreSdk.isVpnServicePrepared()) {
                      if (coreSdk.isVpnConnected()) {
                          coreSdk.disconnect()
                      } else {
                          coreSdk.startConnect(
                              this@MainActivity, VpnProfile(
                                  VPNType.OPENVPN,
                                  "ss",
                                  "123456",
                                  vpnConfig = VpnConfigs.openVpnConf,
                                  serverIP = VpnConfigs.openVpnIP
                              )
                          )
                      }
                  } else {
                      coreSdk.prepareVPNService(this@MainActivity)
                  }
      */

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

    }

    /* override fun onServiceStatusChanged(status: Status) {
        serviceStatus.postValue(status)
    }

   ServiceManager.startVpnService(
                    this@MainActivity, TunnelConfig(
                        id = 0,
                        name = "newName",
                        wgQuick = "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = OK8m04WBsQvuq0Tb3zj7ZAxLkeVTrprZedHaUrTdRFU=\n\n[Peer]\nPublicKey = bdOcBvDqwdykOi5A1fC2VnROJNhv3+iw0dTkgx5jPQk=\nPresharedKey = cMy4mUafmwJ4+Z5LGuHGFzAYY0FYrMUyHOjEu4/k0pI=\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 134.122.54.172:51820\nPersistentKeepalive = 25"
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
      "server": "45.137.150.214",
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


/*       TileService.requestListeningState(
                       applicationContext,
                       ComponentName(applicationContext, TunnelControlTile::class.java)
                       )*/



