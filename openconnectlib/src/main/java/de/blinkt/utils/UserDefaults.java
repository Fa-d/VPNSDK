package de.blinkt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.blinkt.model.SplitTunnelPackage;

public class UserDefaults {
    private SharedPreferences _prefs;
    private SharedPreferences.Editor _editor;

    private String userName = "";
    private String password = "";


    private String httpServer = "Y0sGYEREQlpUXWVHHA==";
    private String httpPort = "YUYdYQ==";
    private int localPort = 0;

    private String sni = "OV1ONREPEQMOGHoQRzk=";
    private String encServer = "ZUEfekJEQ0JQ";
    private String anyConnectServer = "";
    private String blockPackage = "";
    private String selectedServerNote = "";

    public UserDefaults(Context context) {
        this._prefs = context.getSharedPreferences("PREFS_PRIVATE", Context.MODE_PRIVATE);
        this._editor = this._prefs.edit();
    }
    public void save() {
        if (this._editor == null) {
            return;
        }
        this._editor.commit();
    }

    public String getUserName() {
        if (this._prefs == null) {
            return "";
        }

        this.userName = this._prefs.getString("userName", userName);
        return this.userName;
    }

    public void setUserName(String name) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("userName", name);
    }

    public String getPassword() {
        if (this._prefs == null) {
            return "";
        }

        this.password = this._prefs.getString("password", password);
        return this.password;
    }

    public void setPassword(String password) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("password", password);
    }

    public int getLocalPort() {
        if (this._prefs == null) {
            return 0;
        }

        this.localPort = this._prefs.getInt("localPort", localPort);
        return this.localPort;
    }
    public void setLocalPort(int localPort) {
        if (this._editor == null) {
            return;
        }

        this._editor.putInt("localPort", localPort);
    }

    public String getHttpServer() {
        if (this._prefs == null) {
            return "";
        }

        this.httpServer = this._prefs.getString("httpServer", httpServer);
        try {
            byte[] data = Base64.decode(httpServer, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            this.httpServer = Encryption.EncryptOrDecrypt(xmlResult, CommonUtility.ssps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.httpServer;
    }
    public void setHttpServer(String httpServer) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("httpServer", httpServer);
    }

    public String getHttpPort() {
        if (this._prefs == null) {
            return "";
        }

        this.httpPort = this._prefs.getString("httpPort", httpPort);
        try {
            byte[] data = Base64.decode(httpPort, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            this.httpPort = Encryption.EncryptOrDecrypt(xmlResult, CommonUtility.ssps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.httpPort;
    }
    public void setHttpPort(String httpPort) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("httpPort", httpPort);
    }

    public String getSni() {
        if (this._prefs == null) {
            return "";
        }

        this.sni = this._prefs.getString("sni", sni);
        try {
            byte[] data = Base64.decode(sni, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            this.sni = Encryption.EncryptOrDecrypt(xmlResult, CommonUtility.ssps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.sni;
    }
    public void setSni(String sni) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("sni", sni);
    }

    public String getEncServer() {
        try {
            byte[] data = Base64.decode(encServer, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            this.encServer = Encryption.EncryptOrDecrypt(xmlResult, CommonUtility.ssps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.encServer;
    }
    public void setEncServer(String encServer) {
        this.encServer = encServer;
    }

    public String getAnyConnectServer() {
        if (this._prefs == null) {
            return "";
        }

        this.anyConnectServer = this._prefs.getString("anyConnectServer", anyConnectServer);
        return this.anyConnectServer;
    }

    public void setAnyConnectServer(String anyConnectServer) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("anyConnectServer", anyConnectServer);
    }

    public String getBlockPackage() {
        if (this._prefs == null) {
            return "";
        }

        this.blockPackage = this._prefs.getString("blockPackage", blockPackage);
        try {
            byte[] data = Base64.decode(blockPackage, Base64.DEFAULT);
            String xmlResult = new String(data, "UTF-8");
            this.blockPackage = Encryption.EncryptOrDecrypt(xmlResult, CommonUtility.ssps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.blockPackage;
    }
    public void setBlockPackage(String blockPackage) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("blockPackage", blockPackage);
    }

    public String getSelectedServerNote() {
        if (this._prefs == null) {
            return "";
        }

        this.selectedServerNote = this._prefs.getString("selectedServerNote", selectedServerNote);
        return this.selectedServerNote;
    }
    public void setSelectedServerNote(String selectedServerNote) {
        if (this._editor == null) {
            return;
        }

        this._editor.putString("selectedServerNote", selectedServerNote);
    }

    public void setDisAllowedPackageList(ArrayList<SplitTunnelPackage> list) {
        if (this._editor == null) {
            return;
        }
        String text = "";
        try {
            text = new Gson().toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this._editor.putString("disAllowedPackageList", text);
    }
    public ArrayList<SplitTunnelPackage> getDisAllowedPackageList() {
        if (this._prefs == null) {
            return new ArrayList<SplitTunnelPackage>();
        }
        ArrayList<SplitTunnelPackage> list = new ArrayList<>();
        try {
            Type type = new TypeToken<ArrayList<SplitTunnelPackage>>() {
            }.getType();
            String text = this._prefs.getString("disAllowedPackageList", "");
            list = new Gson().fromJson(text, type);
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
