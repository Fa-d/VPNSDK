package com.faddy.tunnelkitlib.interfaces

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}