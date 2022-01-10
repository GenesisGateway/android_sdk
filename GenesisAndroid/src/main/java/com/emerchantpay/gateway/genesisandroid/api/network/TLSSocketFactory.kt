package com.emerchantpay.gateway.genesisandroid.api.network

import android.net.SSLCertificateSocketFactory
import android.util.Log
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.util.*
import java.util.regex.Pattern
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class TLSSocketFactory : SSLSocketFactory {
    private val delegate: SSLSocketFactory
    private val compatible: Boolean

    constructor() {
        this.delegate = SSLCertificateSocketFactory.getDefault(HANDSHAKE_TIMEOUT, null)
        this.compatible = false
    }

    constructor(delegate: SSLSocketFactory) {
        this.delegate = delegate
        this.compatible = false
    }

    constructor(delegate: SSLSocketFactory, compatible: Boolean) {
        this.delegate = delegate
        this.compatible = compatible
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    private fun makeSocketSafe(socket: Socket): Socket {
        var socket = socket
        if (socket is SSLSocket)
            socket = TlsSSLSocket(socket, compatible)
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket? {
        return makeSocketSafe(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket? {
        return makeSocketSafe(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket? {
        return makeSocketSafe(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket? {
        return makeSocketSafe(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress,
                              localPort: Int): Socket? {
        return makeSocketSafe(delegate.createSocket(address, port, localAddress, localPort))
    }

    private inner class TlsSSLSocket(delegate: SSLSocket, internal val compatible: Boolean) : DelegateSSLSocket(delegate) {

        init {

            if (compatible) {
                val protocols = ArrayList(Arrays.asList(*delegate
                        .enabledProtocols))
                protocols.remove("SSLv2")
                protocols.remove("SSLv3")
                super.setEnabledProtocols(protocols.toTypedArray())


                // Exclude weak EXPORT ciphers like null ciphers
                val enabled = ArrayList<String>(10)
                val exclude = Pattern.compile(".*(EXPORT|NULL).*")
                for (cipher in delegate.enabledCipherSuites) {
                    if (!exclude.matcher(cipher).matches()) {
                        enabled.add(cipher)
                    }
                }
                super.setEnabledCipherSuites(enabled.toTypedArray())
            } // else

            // 16-19 support v1.1
            // 20+ support v1.2
            // https://developer.android.com/reference/javax/net/ssl/SSLSocket.html
            val protocols = ArrayList(Arrays.asList(*delegate
                    .supportedProtocols))
            protocols.remove("SSLv2")
            protocols.remove("SSLv3")
            super.setEnabledProtocols(protocols.toTypedArray())

            // Exclude weak ciphers like null ciphers etc.
            val enabledCiphers = ArrayList<String>(10)
            val exclude = Pattern.compile(".*(_DES|DH_|DSS|EXPORT|MD5|NULL|RC4).*")
            for (cipher in delegate.supportedCipherSuites) {
                if (!exclude.matcher(cipher).matches()) {
                    enabledCiphers.add(cipher)
                }
            }
            super.setEnabledCipherSuites(enabledCiphers.toTypedArray())
        }

        // Workaround for Android < 19 where SSLv3 is forced
        override fun setEnabledProtocols(protocols: Array<String>) {
            var protocols = protocols
            if (protocols != null && protocols.size == 1 && "SSLv3" == protocols[0]) {
                val systemProtocols: List<String>
                if (this.compatible) {
                    systemProtocols = Arrays.asList(*delegate.enabledProtocols)
                } else {
                    systemProtocols = Arrays.asList(*delegate.supportedProtocols)
                }
                val enabledProtocols = ArrayList(systemProtocols)
                if (enabledProtocols.size > 1) {
                    enabledProtocols.remove("SSLv2")
                    enabledProtocols.remove("SSLv3")
                } else {
                    Log.w(TAG, "SSL stuck with protocol available for $enabledProtocols")
                }
                protocols = enabledProtocols.toTypedArray()
            }
            super.setEnabledProtocols(protocols)
        }
    }

    companion object {
        private const val HANDSHAKE_TIMEOUT = 0
        private const val TAG = "TLSSocketFactory"
    }
}