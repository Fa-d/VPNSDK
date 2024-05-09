package com.faddy.wgtunlib.data

import com.wireguard.config.Config
import java.io.InputStream


data class TunnelConfig(val id: Int = 0, var name: String, var wgQuick: String) {
    companion object {
        fun configFromQuick(wgQuick: String): Config {
            val inputStream: InputStream = wgQuick.byteInputStream()
            val reader = inputStream.bufferedReader(Charsets.UTF_8)
            return Config.parse(reader)
        }
    }
}
