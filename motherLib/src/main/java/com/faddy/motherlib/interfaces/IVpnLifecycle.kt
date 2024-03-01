package com.faddy.motherlib.interfaces

interface IVpnLifecycle {
    fun onVPNStart()
    fun onVPNResume()
    fun onVPNDestroy()
    fun onVPNPause()
}