package de.blinkt.utils;

import android.content.Context;
import android.util.Base64;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class CommonUtility {
    public static boolean wasConnected = true;
    public static boolean isConnected = false;
    public static boolean isConnecting = false;
    public static boolean isDisconnecting = false;
    public static boolean connectedUsingHttp = false;
    public static boolean isSSH = false;
    public static boolean isOpenConnect = false;


    private static RequestQueue requestQueue;

    public static String ssps = "Ts(Trjslas";
    private static String apiKey = "Jufk8(fds";

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    public static <T> void addToRequestQueue(Context context, Request<T> req) {
//        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //req.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue(context).add(req);
    }

    public static String getDecryptedResponse(String encryptedResponse) {
        String response = "";
        try {
            byte[] data = Base64.decode(encryptedResponse, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            response = Encryption.EncryptOrDecrypt(xmlResult, apiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
