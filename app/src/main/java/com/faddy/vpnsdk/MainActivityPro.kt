package com.faddy.vpnsdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile
import com.faddy.vpnsdk.databinding.ActivityMainBinding

class MainActivityPro : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val coreSdk = MainApp.vpnSdk!!
    override fun onPause() {
        super.onPause()
        coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        coreSdk.onVPNResume(this@MainActivityPro)
    }

    override fun onDestroy() {
        coreSdk.onVPNDestroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        initData()
        initView()
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
        binding.button1.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    coreSdk.startConnect(
                        this@MainActivityPro, VpnProfile(
                            vpnType = VPNType.OPENVPN, userName = "ss", password = "123456",
                            vpnConfig = VpnConfigs.openVpnConf,
                            serverIP = VpnConfigs.openVpnIP
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(this@MainActivityPro)
            }
        }
    }

    private fun initData() {

    }

    private fun initView() {

    }


}