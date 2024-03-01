package com.rbuild.mushroom.injector.phcyber.ssl;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class TcpRelay extends Thread {
    /**
     *
     */
    private final TcpSSLThread tcpProxyServerThread;
    private InputStream in;
    private OutputStream out;
    private String side;
    private int sessionid;
    private final static int BUFSIZ = 4096;
    private byte buf[] = new byte[BUFSIZ];



    public TcpRelay(TcpSSLThread tcpProxyServerThread, InputStream in, OutputStream out, String side, int sessionid) {
        this.tcpProxyServerThread = tcpProxyServerThread;
        this.in = in;
        this.out = out;
        this.side = side;
        this.sessionid = sessionid;


    }

    public void run() {
        int n = 0;

        try {
            while ((n = in.read(buf)) > 0) {
                if (Thread.interrupted()) {
                    // We've been interrupted: no more relaying

                    //Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": Interrupted "+side+" thread");
                    try {

                        out.flush();

                        in.close();
                        out.close();
                    } catch (IOException e) {
                        //Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": "+e.toString());
                    }
                    return;
                }
                out.write(buf, 0, n);
                out.flush();

                for (int i = 0; i < n; i++) {
                    if (buf[i] == 7)
                        buf[i] = '#';

                }

            }
        } catch (SocketException e) {
            //Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": "+e.toString());
        } catch (IOException e) {
            //Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": "+e.toString());
        } finally {
            try {

                out.flush();
                in.close();
                out.close();
            } catch (IOException e) {
                //Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": "+e.toString());
            }
        }
        Log.d("Symlex", this.tcpProxyServerThread.tunnelName+"/"+sessionid+": Quitting "+side+"-side stream proxy...");
    }

}
