package com.rbuild.mushroom.injector.phcyber.ssl;

import java.net.DatagramSocket;
import java.net.Socket;

public class SocketProtect {

    private static IProtectSocket mlistener;

    public interface IProtectSocket {
        boolean protectSocket(DatagramSocket datagramSocket);

        boolean protectSocket(Socket socket);

        void remoteProxy(String str);
    }

    public static void setIProtectSocket(Object obj) {
        if (obj instanceof IProtectSocket) {
            mlistener = (IProtectSocket) obj;
        }
    }

    public static void setRemoteProxy(String str) {
        IProtectSocket iProtectSocket = mlistener;
        if (iProtectSocket != null) {
            iProtectSocket.remoteProxy(str);
        }
    }

    public static boolean isProtected(Socket socket) {
        IProtectSocket iProtectSocket = mlistener;
        if (iProtectSocket != null) {
            return iProtectSocket.protectSocket(socket);
        }
        return false;
    }

    public static boolean isProtected(DatagramSocket datagramSocket) {
        IProtectSocket iProtectSocket = mlistener;
        if (iProtectSocket != null) {
            return iProtectSocket.protectSocket(datagramSocket);
        }
        return false;
    }
}
