package com.faddy.singbox.utils

import io.nekohasekai.libbox.Libbox
import java.io.Closeable

class HTTPClient : Closeable {

    companion object {
        val userAgent by lazy {
            var userAgent = "SFA/"
            userAgent += "100"
            userAgent += " ("
            userAgent += "100"
            userAgent += "; sing-box "
            userAgent += Libbox.version()
            userAgent += ")"
            userAgent
        }
    }

    private val client = Libbox.newHTTPClient()

    init {
        client.modernTLS()
    }

    fun getString(url: String): String {
        val request = client.newRequest()
        request.setUserAgent(userAgent)
        request.setURL(url)
        val response = request.execute()
        return response.contentString
    }

    override fun close() {
        client.close()
    }


}