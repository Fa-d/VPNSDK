package com.faddy.vpnsdk.ui.splitTunnel

import android.graphics.drawable.Drawable

data class AppModel (
    var appName: String,
    var packageName: String,
    var appIcon: Drawable,
    var isAllowed: Boolean = false
)