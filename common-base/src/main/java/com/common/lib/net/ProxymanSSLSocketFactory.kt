package com.common.lib.net

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.*
import javax.net.ssl.*

class ProxymanSSLSocketFactory : SSLSocketFactory() {
    private val delegate: SSLSocketFactory by lazy {
        val trustAllCerts = arrayOf<TrustManager>(TrustAllCerts())
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        sslContext.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> = delegate.defaultCipherSuites
    override fun getSupportedCipherSuites(): Array<String> = delegate.supportedCipherSuites

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return delegate.createSocket(s, host, port, autoClose)
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return delegate.createSocket(host, port)
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return delegate.createSocket(host, port, localHost, localPort)
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int): Socket {
        return delegate.createSocket(address, port)
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return delegate.createSocket(address, port, localAddress, localPort)
    }
}

class TrustAllCerts : X509TrustManager {
    override fun checkClientTrusted(
        chain: Array<java.security.cert.X509Certificate>,
        authType: String
    ) {
    }

    override fun checkServerTrusted(
        chain: Array<java.security.cert.X509Certificate>,
        authType: String
    ) {
    }

    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
}