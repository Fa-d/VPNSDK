package com.faddy.vpnsdk

//import com.faddy.wgtunlib.data.model.TunnelConfig
//import com.faddy.wgtunlib.service.foreground.ServiceManager
//import com.faddy.wgtunlib.service.tile.TunnelControlTile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.faddy.vpnsdk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    //private val coreSdk = MainApp.vpnSdk!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        initData()
        initView()
        initClickListener()
        initObserver()
    }

    private fun initObserver() {

    }

    private fun initView() {


    }

    override fun onPause() {
        super.onPause()
        // coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        // coreSdk.onVPNResume(this@MainActivity)
    }

    private fun initClickListener() {
        binding.button1.setOnClickListener {

        }
        binding.button2.setOnClickListener {
        }
    }


    private fun initData() {
        // Use mmkv here
        //coreSdk.getConnectedTime()

    }

    override fun onDestroy() {
        // coreSdk.onVPNDestroy()
        super.onDestroy()
    }




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


/*       TileService.requestListeningState(
                       applicationContext,
                       ComponentName(applicationContext, TunnelControlTile::class.java)
                       )*/


///singbox implementataion

/***
class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
lateinit var singboxCore: SingBoxCore

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
singboxCore = SingBoxCore(applicationContext, this)
initView()
initClickListener()
singboxCore.reconnect(this)
}

private fun initView() {
singboxCore?.serviceStatus?.observe(this) { value ->
binding.connectionStatus.text = value.name
}
singboxCore?.uploadDiff?.observe(this) { value ->
binding.uploadText.text = value
}
singboxCore?.downloadDiff?.observe(this) { value ->
binding.downloadText.text = value
}
}

private fun initClickListener() {
binding.button1.setOnClickListener {
singboxCore?.startConnect()
}
}

override fun onDestroy() {
singboxCore?.onDestroy()
super.onDestroy()
}
}
 */
