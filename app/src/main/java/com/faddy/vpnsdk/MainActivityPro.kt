package com.faddy.vpnsdk

import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.faddy.phoenixlib.PhoenixVPN
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.vpnsdk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivityPro : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var coreSdk: PhoenixVPN

    override fun onPause() {
        super.onPause()
        coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        coreSdk.onVPNResume()
    }

    override fun onDestroy() {
        coreSdk.onVPNDestroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        coreSdk.onVpnCreate()
        initClickListener()
        initObserver()
    }

    private fun initObserver() {
        coreSdk.funInvoker = {
            coreSdk.myCurrentIp?.observe(this) { text ->
                binding.ipText.text = text
            }

            coreSdk.currentUploadSpeed?.observe(this) { text ->
                binding.uploadText.text = text.toString()
            }

            coreSdk.currentDownloadSpeed?.observe(this) { text ->
                binding.downloadText.text = text.toString()
            }

            coreSdk.connectedVpnTime.observe(this) { text ->
                binding.timerText.text = text.toString()
            }

            coreSdk.currentPing.observe(this) { text ->
                binding.pingTv.text = "$text ms"
            }

            coreSdk.connectedStatus?.observe(this) { status ->
                Log.e("getVpnConnectedStatus", status.name)
                when (status) {
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
            }
        }
    }

    private fun initClickListener() {
        binding.buttonWireguard.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    coreSdk.startConnect(
                        this@MainActivityPro, VpnProfile(
                            vpnType = VPNType.WIREGUARD, userName = "ss", password = "123456",
                            vpnConfig = VpnConfigs.wgTunnelConfig,
                            serverIP = VpnConfigs.openVpnIP
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(this@MainActivityPro, activityRes)
            }
        }
        binding.buttonSingbox.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    val data = android.util.Base64.decode(
                        VpnConfigs.singBoxConfigEnc, android.util.Base64.DEFAULT
                    )
                    val configText = String(data, Charsets.UTF_8)
                    coreSdk.startConnect(
                        this@MainActivityPro, VpnProfile(
                            vpnType = VPNType.SINGBOX,
                            userName = "ss",
                            password = "123456",
                            vpnConfig = VpnConfigs.wireGuardJson,
                            // vpnConfig = configText,
                            serverIP = VpnConfigs.openVpnIP
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(this@MainActivityPro, activityRes)
            }
        }
        binding.buttonValidity.setOnClickListener {
            Log.e("aedfghn", packageName);
            Process.killProcess(Process.myPid())
            System.exit(1)
        }
        binding.buttonOvpn.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    val data = android.util.Base64.decode(
                        VpnConfigs.singBoxConfigEnc, android.util.Base64.DEFAULT
                    )
                    val configText = String(data, Charsets.UTF_8)
                    coreSdk.startConnect(
                        this@MainActivityPro, VpnProfile(
                            vpnType = VPNType.OPENVPN, userName = "test1", password = "1111112",
                            // vpnConfig = VpnConfigs.wireGuardJson,
                            vpnConfig = VpnConfigs.openVpnConf, serverIP = VpnConfigs.openVpnIP
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(this@MainActivityPro, activityRes)
            }
        }
    }

    private val activityRes =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // startConn()
        }
}