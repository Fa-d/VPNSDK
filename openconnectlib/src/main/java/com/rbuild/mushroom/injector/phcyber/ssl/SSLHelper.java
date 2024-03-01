package com.rbuild.mushroom.injector.phcyber.ssl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class SSLHelper {

	private static TcpSSLThread server;
	public static boolean isRunning = false;
	public static boolean usingHttp = false;

	public static void runSSL(Context c, int lp, String host, int sp, String sni){
		server = new TcpSSLThread("openvpn", lp,  host,
				sp,sni, null, null, null);
		server.start();
		isRunning = true;
	}

	public static void runHelper(Context c){
/*		UserDefaults defaults = new UserDefaults(c);
		defaults.setLocalPort(nextFreePort());
		defaults.save();
		server = new TcpSSLThread("openvpn", defaults.getLocalPort(),  defaults.getHttpServer(),
				Integer.parseInt(defaults.getHttpPort()), defaults.getSni(), null, null, null);
		server.start();
		isRunning = true;*/
	}

	public static void stopSSL(Context c){
		try {
			server.ss.close();
			server.sc.close();
			if(server.st != null){
				server.st.close();
			}
			server.interrupt();
			server.stop();
			isRunning = false;
		} catch (Exception e) {
			Log.d("Symlex", "Interrupt failure: " + e.toString());
		}
	}

	private static int nextFreePort() {
		int port = new Random().nextInt(1000) + 11999;
		while (true) {
			if (isLocalPortFree(port)) {
				return port;
			} else {
				port = new Random().nextInt(1000) + 11999;
			}
		}
	}

	private static boolean isLocalPortFree(int port) {
		try {
			new ServerSocket(port).close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}



	public static boolean isHttpConnected(Context context, boolean forceUpdate){
		final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connMgr != null) {
			NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if(activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
					if(forceUpdate){
						return checkHttp();
					}else{
						if(!usingHttp){
							return checkHttp();
						}else{
							return true;
						}
					}
				}else{
					usingHttp = false;
					return false;
				}
			}
		}
		return false;
	}

	private static boolean checkHttp(){
		try {
			Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 www.google.com");
			int returnVal = p1.waitFor();
			boolean reachable = (returnVal==0);
			if(reachable){
				usingHttp = false;
				return false;
			}else{
				usingHttp = true;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}