package com.emerchantpay.gateway.genesisandroid.api.network

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.SocketAddress
import java.net.SocketException
import java.nio.channels.SocketChannel

import javax.net.ssl.HandshakeCompletedListener
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocket

open class DelegateSSLSocket internal constructor(protected val delegate: SSLSocket) : SSLSocket() {

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    override fun getEnabledCipherSuites(): Array<String> {
        return delegate.enabledCipherSuites
    }

    override fun setEnabledCipherSuites(suites: Array<String>) {
        delegate.enabledCipherSuites = suites
    }

    override fun getSupportedProtocols(): Array<String> {
        return delegate.supportedProtocols
    }

    override fun getEnabledProtocols(): Array<String> {
        return delegate.enabledProtocols
    }

    override fun setEnabledProtocols(protocols: Array<String>) {
        delegate.enabledProtocols = protocols
    }

    override fun getSession(): SSLSession {
        return delegate.session
    }

    override fun addHandshakeCompletedListener(listener: HandshakeCompletedListener) {
        delegate.addHandshakeCompletedListener(listener)
    }

    override fun removeHandshakeCompletedListener(listener: HandshakeCompletedListener) {
        delegate.removeHandshakeCompletedListener(listener)
    }

    @Throws(IOException::class)
    override fun startHandshake() {
        delegate.startHandshake()
    }

    override fun setUseClientMode(mode: Boolean) {
        delegate.useClientMode = mode
    }

    override fun getUseClientMode(): Boolean {
        return delegate.useClientMode
    }

    override fun setNeedClientAuth(need: Boolean) {
        delegate.needClientAuth = need
    }

    override fun setWantClientAuth(want: Boolean) {
        delegate.wantClientAuth = want
    }

    override fun getNeedClientAuth(): Boolean {
        return delegate.needClientAuth
    }

    override fun getWantClientAuth(): Boolean {
        return delegate.wantClientAuth
    }

    override fun setEnableSessionCreation(flag: Boolean) {
        delegate.enableSessionCreation = flag
    }

    override fun getEnableSessionCreation(): Boolean {
        return delegate.enableSessionCreation
    }

    @Synchronized
    @Throws(IOException::class)
    override fun close() {
        delegate.close()
    }

    @Throws(IOException::class)
    override fun connect(remoteAddr: SocketAddress) {
        delegate.connect(remoteAddr)
    }

    @Throws(IOException::class)
    override fun connect(remoteAddr: SocketAddress, timeout: Int) {
        delegate.connect(remoteAddr, timeout)
    }

    override fun getChannel(): SocketChannel {
        return delegate.channel
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return delegate.inputStream
    }

    override fun getLocalAddress(): InetAddress {
        return delegate.localAddress
    }

    override fun getLocalPort(): Int {
        return delegate.localPort
    }

    override fun getLocalSocketAddress(): SocketAddress {
        return delegate.localSocketAddress
    }

    @Throws(IOException::class)
    override fun getOutputStream(): OutputStream {
        return delegate.outputStream
    }

    override fun getPort(): Int {
        return delegate.port
    }

    @Synchronized
    @Throws(SocketException::class)
    override fun getReceiveBufferSize(): Int {
        return delegate.receiveBufferSize
    }

    @Synchronized
    @Throws(SocketException::class)
    override fun getSendBufferSize(): Int {
        return delegate.sendBufferSize
    }

    @Synchronized
    @Throws(SocketException::class)
    override fun getSoTimeout(): Int {
        return delegate.soTimeout
    }

    override fun isClosed(): Boolean {
        return delegate.isClosed
    }

    override fun isConnected(): Boolean {
        return delegate.isConnected
    }

    override fun isInputShutdown(): Boolean {
        return delegate.isInputShutdown
    }

    override fun isOutputShutdown(): Boolean {
        return delegate.isOutputShutdown
    }

    @Synchronized
    @Throws(SocketException::class)
    override fun setReceiveBufferSize(size: Int) {
        delegate.receiveBufferSize = size
    }

    @Synchronized
    @Throws(SocketException::class)
    override fun setSendBufferSize(size: Int) {
        delegate.sendBufferSize = size
    }

    @Throws(IOException::class)
    override fun shutdownOutput() {
        delegate.shutdownOutput()
    }

    fun setHostname(host: String): DelegateSSLSocket {
        try {
            delegate.javaClass
                    .getMethod("setHostname", String::class.java)
                    .invoke(delegate, host)
        } catch (e: Exception) {
            throw IllegalStateException("Could not enable SNI", e)
        }

        return this
    }
}
