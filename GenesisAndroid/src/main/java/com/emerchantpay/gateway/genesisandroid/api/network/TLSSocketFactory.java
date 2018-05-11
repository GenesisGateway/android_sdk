package com.emerchantpay.gateway.genesisandroid.api.network;

import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TLSSocketFactory extends SSLSocketFactory {
    private static final int HANDSHAKE_TIMEOUT=0;
    private static final String TAG = "TLSSocketFactory";
    private final SSLSocketFactory delegate;
    private final boolean compatible;

    public TLSSocketFactory() {
        this.delegate =SSLCertificateSocketFactory.getDefault(HANDSHAKE_TIMEOUT, null);
        this.compatible = false;
    }

    public TLSSocketFactory(SSLSocketFactory delegate) {
        this.delegate = delegate;
        this.compatible = false;
    }

    public TLSSocketFactory(SSLSocketFactory delegate, boolean compatible) {
        this.delegate = delegate;
        this.compatible = compatible;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    private Socket makeSocketSafe(Socket socket, String host) {
        if (socket instanceof SSLSocket) {
            TlsSSLSocket tempSocket=
                    new TlsSSLSocket((SSLSocket) socket, compatible);

            if (delegate instanceof SSLCertificateSocketFactory &&
                    Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((android.net.SSLCertificateSocketFactory)delegate)
                        .setHostname(socket, host);
            }
            else {
                tempSocket.setHostname(host);
            }

            socket = tempSocket;
        }
        return socket;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException {
        return makeSocketSafe(delegate.createSocket(s, host, port, autoClose), host);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return makeSocketSafe(delegate.createSocket(host, port), host);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException {
        return makeSocketSafe(delegate.createSocket(host, port, localHost, localPort), host);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return makeSocketSafe(delegate.createSocket(host, port), host.getHostName());
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
                               int localPort) throws IOException {
        return makeSocketSafe(delegate.createSocket(address, port, localAddress, localPort),
                address.getHostName());
    }

    private class TlsSSLSocket extends DelegateSSLSocket {

        final boolean compatible;

        private TlsSSLSocket(SSLSocket delegate, boolean compatible) {
            super(delegate);
            this.compatible = compatible;

            if (compatible) {
                ArrayList<String> protocols = new ArrayList<String>(Arrays.asList(delegate
                        .getEnabledProtocols()));
                protocols.remove("SSLv2");
                protocols.remove("SSLv3");
                super.setEnabledProtocols(protocols.toArray(new String[protocols.size()]));


                // Exclude weak EXPORT ciphers like null ciphers
                ArrayList<String> enabled = new ArrayList<String>(10);
                Pattern exclude = Pattern.compile(".*(EXPORT|NULL).*");
                for (String cipher : delegate.getEnabledCipherSuites()) {
                    if (!exclude.matcher(cipher).matches()) {
                        enabled.add(cipher);
                    }
                }
                super.setEnabledCipherSuites(enabled.toArray(new String[enabled.size()]));
                return;
            } // else

            // 16-19 support v1.1
            // 20+ support v1.2
            // https://developer.android.com/reference/javax/net/ssl/SSLSocket.html
            ArrayList<String> protocols = new ArrayList<String>(Arrays.asList(delegate
                    .getSupportedProtocols()));
            protocols.remove("SSLv2");
            protocols.remove("SSLv3");
            super.setEnabledProtocols(protocols.toArray(new String[protocols.size()]));

            // Exclude weak ciphers like null ciphers etc.
            ArrayList<String> enabledCiphers = new ArrayList<String>(10);
            Pattern exclude = Pattern.compile(".*(_DES|DH_|DSS|EXPORT|MD5|NULL|RC4).*");
            for (String cipher : delegate.getSupportedCipherSuites()) {
                if (!exclude.matcher(cipher).matches()) {
                    enabledCiphers.add(cipher);
                }
            }
            super.setEnabledCipherSuites(enabledCiphers.toArray(new String[enabledCiphers.size()]));
        }

        // Workaround for Android < 19 where SSLv3 is forced
        @Override
        public void setEnabledProtocols(String[] protocols) {
            if (protocols != null && protocols.length == 1 && "SSLv3".equals(protocols[0])) {
                List<String> systemProtocols;
                if (this.compatible) {
                    systemProtocols = Arrays.asList(delegate.getEnabledProtocols());
                } else {
                    systemProtocols = Arrays.asList(delegate.getSupportedProtocols());
                }
                List<String> enabledProtocols = new ArrayList<String>(systemProtocols);
                if (enabledProtocols.size() > 1) {
                    enabledProtocols.remove("SSLv2");
                    enabledProtocols.remove("SSLv3");
                } else {
                    Log.w(TAG, "SSL stuck with protocol available for "
                            + String.valueOf(enabledProtocols));
                }
                protocols = enabledProtocols.toArray(new String[enabledProtocols.size()]);
            }
            super.setEnabledProtocols(protocols);
        }
    }
}