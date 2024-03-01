package com.rbuild.mushroom.injector.phcyber.ssl;


import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TcpSSLThread extends Thread {

    String tunnelName;
    int listenPort;
    String tunnelHost;
    int tunnelPort;
    String keyFile, keyPass, caFile;
    TcpRelay inRelay, outRelay;
    public static ServerSocket ss = null;
    int sessionid = 0;
    private SSLSocketFactory sslSocketFactory;
    private X509Certificate caCert;


//    private String[] TLS13_CIPHER_SUITES = new String[]{
//		"TLS_AES_128_GCM_SHA256",
//		"TLS_AES_256_GCM_SHA384",
//	    "TLS_CHACHA20_POLY1305_SHA256",
//		"TLS_AES_128_CCM_SHA256",
//		"TLS_AES_256_CCM_8_SHA256"
//	};

    private String sni;

    public static Socket sc;

    public static SSLSocket st;
    public TcpSSLThread(String tunnelName, int listenPort, String tunnelHost, int tunnelPort, String nsni, String keyFile, String keyPass, String caFile) {
        this.tunnelName = tunnelName ;
        this.listenPort = listenPort;
        this.tunnelHost = tunnelHost;
        this.tunnelPort = tunnelPort;
        this.sni = nsni;
        this.keyFile = keyFile;
        this.keyPass = keyPass;
        this.caFile = caFile;

        // Loading the CA cert
        if (caFile != null && !caFile.isEmpty()) {
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(this.caFile);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                caCert = (X509Certificate) cf.generateCertificate(inStream);

            } catch (Exception ex) {
                ex.printStackTrace();
                //FIXME
            } finally {
                try {
                    if (inStream != null)
                        inStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    TrustManager[] trustCaCert = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                        X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                        X509Certificate[] certs, String authType) throws CertificateException {

                    if (caFile == null || caFile.isEmpty()) //No CA file - trust all
                        return;

                    if (certs == null || certs.length == 0) {
                        throw new IllegalArgumentException("null or zero-length certificate chain");
                    }

                    if (authType == null || authType.length() == 0) {
                        throw new IllegalArgumentException("null or zero-length authentication type");
                    }

                    if (caCert == null) { //CA file specified, but no CA cert loaded
                        throw new CertificateException("Invalid CA cert");
                    }

                    //Check if top-most cert is our CA's
                    if(!certs[0].equals(caCert)){
                        try
                        {   //Not our CA's. Check if it has been signed by our CA
                            certs[0].verify(caCert.getPublicKey());
                        }
                        catch(Exception e){
                            throw new CertificateException("Certificate not trusted",e);
                        }
                    }

                    //If we end here certificate is trusted. Check if any cert in the chain has expired.
                    try{
                        for (X509Certificate cert : certs) {
                            cert.checkValidity();
                        }
                    }
                    catch(Exception e){
                        throw new CertificateException("Certificate not trusted. It has expired",e);
                    }
                }
            }
    };


    public final SSLSocketFactory getSocketFactory(String pkcsFile,
                                                   String pwd, int sessionid) {
        if (sslSocketFactory == null) {
            try {
                KeyManagerFactory keyManagerFactory;
                if (pkcsFile != null && !pkcsFile.isEmpty()) {
                    keyManagerFactory = KeyManagerFactory.getInstance("X509");
                    KeyStore keyStore = KeyStore.getInstance("PKCS12");
                    keyStore.load(new FileInputStream(pkcsFile), pwd.toCharArray());
                    keyManagerFactory.init(keyStore, pwd.toCharArray());
                } else {
                    keyManagerFactory = null;
                }
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers(), trustCaCert,
                        new SecureRandom());
                sslSocketFactory = context.getSocketFactory();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }

        return sslSocketFactory;
    }


    public void run() {
        try {
            InetAddress bindAddr = Inet4Address.getByAddress(new byte[] { 127, 0, 0, 1 });
            ss = new ServerSocket(listenPort,50,bindAddr);
            //log("SSL service is running");

        } catch (Exception e) {
            e.printStackTrace();
            //log("SSL service is failed to run");
            return;
        }
        while (true) {
            try {
                Thread fromBrowserToServer = null;
                Thread fromServerToBrowser = null;

                if (isInterrupted()){
                    ss.close();
                    return;
                }

                try {
                    sc = ss.accept();
                    sc.setTcpNoDelay(true);
                    SocketProtect.isProtected(sc);
                    sessionid++;
                } catch (SocketException e) {
                    e.printStackTrace();
                }


                try {
                    final SSLSocketFactory sf = getSocketFactory(this.keyFile, this.keyPass, this.sessionid);
                    //TLSSocketFactory sf = new TLSSocketFactory();
                    st = (SSLSocket) sf.createSocket(this.tunnelHost, this.tunnelPort);
                    st.setTcpNoDelay(true);
                    //st = (SSLSocket) sf.createSocket(cc.getProxy(),cc.getProxyPort() );

                    setSNIHost(sf, (SSLSocket) st,this.sni);
                    st.addHandshakeCompletedListener(new HandshakeCompletedListener(){

                        @Override
                        public void handshakeCompleted(HandshakeCompletedEvent p1)
                        {

                            String ci = p1.getCipherSuite();
                            String tls = p1.getSession().getProtocol();
                            String ph = p1.getSession().getPeerHost();
                            int pp = p1.getSession().getPeerPort();

                            String l = "<b>Established "+tls+" "+"connection with "+ph+":"+pp +ci;
                            if(ph != null){
                                //log(l);
                                try
                                {
                                    String pr = p1.getPeerPrincipal().toString();

                                    //log("Peer Principal: "+pr);
                                }
                                catch (SSLPeerUnverifiedException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }

                    });




                    st.setKeepAlive(true);
                    ((SSLSocket) st).startHandshake();
                    SocketProtect.isProtected(st);


                    //SSL Payload
					/*String mHost = cc.getSSHHost();
					 String pload = cc.getPayload().replace("[crlf]","\r\n")
					 .replace("[host_port]",mHost+":"+cc.getSSLPort())
					 .replace("[crlf*2]","\r\n\r\n")
					 .replace("[host]",mHost)
					 .replace("[port]",String.valueOf(cc.getSSLPort()))
					 .replace("[protocol]","HTTP/1.0")
					 .replace("[raw]", "CONNECT "+mHost+":"+cc.getSSLPort()+" "+"HTTP/1.0\r\n")
					 .replace("[real_raw]", "CONNECT "+mHost+":"+cc.getSSLPort()+" "+"HTTP/1.0\r\n\r\n")
					 .replace("[netData]", "CONNECT "+mHost+":"+cc.getSSLPort()+" "+"HTTP/1.0\r\n\r\n")
					 .replace("[realData]", "CONNECT "+mHost+":"+cc.getSSLPort()+" "+"HTTP/1.0\r\n\r\n")
					 .replace("[ua]","Mozilla/7.0+ (compatible; MSIE 4.01; windows NT 5.0)");
					 OutputStream ost = st.getOutputStream();
					 ost.write(pload.getBytes());
					 ost.flush();*/


                    if(sc!=null){

                    }
					/*if(st != null){
					 doVpnProtect((Socket)st);
					 }*/
                } catch (IOException e) {
                    return;
                }
                catch (Exception e) {
                    if (sc != null)
                    {
                        sc.close();
                    }
                    return;
                }

                if (sc == null || st == null) {
                    return;
                }

                fromBrowserToServer = new TcpRelay(
                        this, sc.getInputStream(), st.getOutputStream(), "client", sessionid);
                fromServerToBrowser = new TcpRelay(
                        this, st.getInputStream(), sc.getOutputStream(), "server", sessionid);

                fromBrowserToServer.start();
                fromServerToBrowser.start();

            } catch (IOException ee) {
            }
        }
    }

    private void setSNIHost(final SSLSocketFactory factory, final SSLSocket socket, final String hostname) {
        if (factory instanceof android.net.SSLCertificateSocketFactory && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((android.net.SSLCertificateSocketFactory)factory).setHostname(socket, hostname);
        } else {
            try {
                socket.getClass().getMethod("setHostname", String.class).invoke(socket, hostname);
            } catch (Throwable e) {
                // ignore any error, we just can't set the hostname...
            }
        }
    }


};