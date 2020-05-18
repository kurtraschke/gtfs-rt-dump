package com.kurtraschke.gtfsrtdump.utils

import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.SSLEngine
import javax.net.ssl.X509ExtendedTrustManager

class NullX509ExtendedTrustManager : X509ExtendedTrustManager() {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String, socket: Socket) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String, socket: Socket) {
    }

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String, engine: SSLEngine) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String, engine: SSLEngine) {
    }

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}