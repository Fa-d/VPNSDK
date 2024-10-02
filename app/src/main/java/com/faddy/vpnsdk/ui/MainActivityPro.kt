package com.faddy.vpnsdk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.faddy.phoenixlib.PhoenixVPN
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
    }



}