package de.blinkt.service;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.VpnService;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.rbuild.mushroom.injector.phcyber.ssl.SocketProtect;

import org.infradead.libopenconnect.LibOpenConnect;
import org.infradead.libopenconnect.LibOpenConnect.VPNStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import de.blinkt.activity.GrantPermissionsActivity;
import de.blinkt.openconnect.R;
import de.blinkt.openconnect.VpnProfile;
import de.blinkt.openconnect.core.DeviceStateReceiver;
import de.blinkt.openconnect.core.KeepAlive;
import de.blinkt.openconnect.core.OpenConnectManagementThread;
import de.blinkt.openconnect.core.OpenVPNManagement;
import de.blinkt.openconnect.core.ProfileManager;
import de.blinkt.openconnect.core.UserDialog;
import de.blinkt.openconnect.core.VPNConnector;
import de.blinkt.openconnect.core.VPNLog;
import de.blinkt.utils.CommonUtility;

public class OpenConnectService extends VpnService implements SocketProtect.IProtectSocket{

	private PowerManager.WakeLock wl;
	public static final String TAG = "OpenConnect";

	public static final String START_SERVICE = "app.openconnect.START_SERVICE";
	public static final String START_SERVICE_STICKY = "app.openconnect.START_SERVICE_STICKY";
	public static final String ALWAYS_SHOW_NOTIFICATION = "app.openconnect.NOTIFICATION_ALWAYS_VISIBLE";

	public static final String ACTION_VPN_STATUS = "app.openconnect.VPN_STATUS";
	public static final String EXTRA_CONNECTION_STATE = "app.openconnect.connectionState";
	public static final String EXTRA_UUID = "app.openconnect.UUID";
	public static final String NOTIFICATION_CHANNEL_BG_ID = "openconnect_bg";
	public static final String NOTIFICATION_CHANNEL_NEWSTATUS_ID = "openconnect_newstat";

	// These are valid in the CONNECTED state
	public VpnProfile profile;
	public LibOpenConnect.IPInfo ipInfo;
	public String serverName;
	public Date startTime;
	public Handler disconnectHandler;

	private DeviceStateReceiver mDeviceStateReceiver;
	private SharedPreferences mPrefs;

	private KeepAlive mKeepAlive;

	private final IBinder mBinder = new LocalBinder();

	private String mUUID;
	private int mStartId;
	Timer t;
	private Thread mVPNThread;
	private OpenConnectManagementThread mVPN;

	private UserDialog mDialog;
	private Context mDialogContext;

	private final int NOTIFICATION_ID = 1;
	private int mActivityConnections;
	private boolean mNotificationActive;

	private int mConnectionState = OpenConnectManagementThread.STATE_DISCONNECTED;
	private String mConnectionStateNames[];
	private VPNStats mStats = new VPNStats();
	private String lastChannel;
	private VPNLog mVPNLog = new VPNLog();
	private Handler mHandler = new Handler();

	private BroadcastReceiver notificationDisconnectReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", intent.getIntExtra("key_openconnect_broadcast",0)+"");
			if (intent.getIntExtra("key_openconnect_broadcast",0) == 4) {
				killVPNThread(true);
				ProfileManager.setConnectedVpnProfileDisconnected();
				stopWakeLock();
				CommonUtility.isConnected = false;
			}
		}
	};

	public void remoteProxy(String str) {
	}

	public boolean protectSocket(Socket socket) {
		return socket != null && protect(socket);
	}

	public boolean protectSocket(DatagramSocket datagramSocket) {
		return protect(datagramSocket);
	}

	public class LocalBinder extends Binder {
		public OpenConnectService getService() {
			// Return this instance of LocalService so clients can call public methods
			return OpenConnectService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		String action = intent.getAction();
		if( action !=null && action.equals(START_SERVICE))
			return mBinder;
		else
			return super.onBind(intent);
	}

	@Override
	public void onRevoke() {
		Log.i(TAG, "VPN access has been revoked");
		stopVPN();
	}

	@Override
	public void onCreate() {
		SocketProtect.setIProtectSocket(this);
		// Restore service state from disk if available
		// This gets overwritten if somebody calls startService()
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mUUID = mPrefs.getString("service_mUUID", "");

		mVPNLog.restoreFromFile(getCacheDir().getAbsolutePath() + "/logdata.ser");
		mConnectionStateNames = getResources().getStringArray(R.array.connection_states);
	}

	@Override
	public void onDestroy() {
		killVPNThread(true);
		if (mDeviceStateReceiver != null) {
			this.unregisterReceiver(mDeviceStateReceiver);
		}
		mVPNLog.saveToFile(getCacheDir().getAbsolutePath() + "/logdata.ser");
		stopWakeLock();

		try {
			if (notificationDisconnectReceiver != null) {
				unregisterReceiver(notificationDisconnectReceiver);
			}
		} catch (Exception e) {
			Log.e(TAG, "onDestroy: unregisterReceiver notificationDisconnectReceiver error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private synchronized boolean doStopVPN() {

		if (mVPN != null) {
			mVPN.stopVPN();
			return true;
		}
		return false;
	}

	private void killVPNThread(boolean joinThread) {


		if (doStopVPN() && joinThread) {
			try {
				mVPNThread.join(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private PendingIntent getMainActivityIntent() {
		// Touching "Configure" on the system VPN dialog will restore the app
		// (same as clicking the launcher icon)
		Intent intent = new Intent(getBaseContext(), GrantPermissionsActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		PendingIntent startLW = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			startLW = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		} else {
			startLW = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		return startLW;
	}

	private void registerDeviceStateReceiver(OpenVPNManagement management) {
		// Registers BroadcastReceiver to track network connection changes.
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(DeviceStateReceiver.PREF_CHANGED);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		mDeviceStateReceiver = new DeviceStateReceiver(management, mPrefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			registerReceiver(mDeviceStateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
		}else {
			registerReceiver(mDeviceStateReceiver, filter);
		}
	}

	private synchronized void registerKeepAlive() {
		String DNSServer = "8.8.8.8";
		try {
			String dns = ipInfo.DNS.get(0);
			if (InetAddress.getByName(dns) != null) {
				DNSServer = dns;
			}
		} catch (IndexOutOfBoundsException e) {
			/* empty DNS server list */
		} catch (Exception e) {
			Log.i(TAG, "server DNS IP is bogus, falling back to " + DNSServer + " for KeepAlive", e);
		}

		int idle = 1800;
		try {
			int val = Integer.parseInt(ipInfo.CSTPOptions.get("X-CSTP-Idle-Timeout"));
			if (val >= 60 && val <= 7200) {
				idle = val;
			}
		} catch (Exception e) {
		}

		// set to 40% of the idle timeout value, to buy a little margin in case
		// the first 1-2 attempts fail
		idle = idle * 4 / 10;
		Log.d(TAG, "calculated KeepAlive interval: " + idle + " seconds");

		IntentFilter filter = new IntentFilter(KeepAlive.ACTION_KEEPALIVE_ALARM);
		mKeepAlive = new KeepAlive(idle, DNSServer, mDeviceStateReceiver);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			registerReceiver(mKeepAlive, filter, Context.RECEIVER_NOT_EXPORTED);
		}else{
			registerReceiver(mKeepAlive, filter);
		}
		mKeepAlive.start(this);
	}

	private void unregisterReceivers() {
		try {
			if (mDeviceStateReceiver != null) {
				unregisterReceiver(mDeviceStateReceiver);
			}
			mDeviceStateReceiver = null;
		} catch (IllegalArgumentException iae) {
			// catch "Receiver not registered" error
			Log.e(TAG, "unregisterReceivers: cDeviceStateReceiver: error: " + iae.getMessage());
		}

		try {
			if (mKeepAlive != null) {
				mKeepAlive.stop(this);
				unregisterReceiver(mKeepAlive);
			}
			mKeepAlive = null;
		} catch (IllegalArgumentException iae) {
			Log.e(TAG, "unregisterReceivers: KeepAlive: error: " + iae.getMessage());
		}

		try {
			if (notificationDisconnectReceiver != null) {
				unregisterReceiver(notificationDisconnectReceiver);
			}
		} catch (Exception e) {
			Log.e(TAG, "unregisterReceivers: notificationDisconnectReceiver: error: " + e.getMessage());
//			e.printStackTrace();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SocketProtect.setIProtectSocket(this);

		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}

		String action = intent.getAction();
		if (START_SERVICE.equals(action)) {
			return START_NOT_STICKY;
		} else if (START_SERVICE_STICKY.equals(action)) {
			return START_REDELIVER_INTENT;
		}

		// Extract information from the intent.
		mUUID = intent.getStringExtra(EXTRA_UUID);
		Log.d(TAG, "onStartCommand: mUUID: " + mUUID);
		if (mUUID == null) {
			return START_NOT_STICKY;
		}
		mPrefs.edit().putString("service_mUUID", mUUID).apply();

		profile = ProfileManager.get(mUUID);
		if (profile == null) {
			return START_NOT_STICKY;
		}

		killVPNThread(true);

		// stopSelfResult(most_recent_startId) will kill the service
		// stopSelfResult(previous_startId) will not
		mStartId = startId;

		mVPN = new OpenConnectManagementThread(getApplicationContext(), profile, this);
		Log.d(TAG, "onStartCommand: mVPN: initialized");
		mVPNThread = new Thread(mVPN, "OpenConnectManagementThread");
		mVPNThread.start();

		unregisterReceivers();
		registerDeviceStateReceiver(mVPN);
		registerNotificationDisconnect();

		ProfileManager.setConnectedVpnProfile(profile);

		return START_NOT_STICKY;
	}

	private void registerNotificationDisconnect() {
		IntentFilter intentFilter = new IntentFilter("app.kloud.vpn.action.service");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			registerReceiver(notificationDisconnectReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
		}else{
			registerReceiver(notificationDisconnectReceiver, intentFilter);
		}
	}

	public Builder getVpnServiceBuilder() {
		Builder b = new Builder();
		b.setSession(profile.mName);
		b.setConfigureIntent(getMainActivityIntent());
		return checkBlockedPackages(b);
	}

	private Builder checkBlockedPackages(Builder b){
		try{
			/*UserDefaults defaults = new UserDefaults(getApplicationContext());
			// Add Blocked Packages From Firebase
			String packages = defaults.getBlockPackage();
			String serverNote = defaults.getSelectedServerNote();
			String[] list = packages.split(",");
			if(list.length > 0 && !serverNote.contains("P2P") && !serverNote.contains("Torrent")){
				for (String s : list) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						try {
							b.addDisallowedApplication(s);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}*/

			// Add DisAllowed Packages By User
			/*ArrayList<SplitTunnelPackage> disAllowedList = defaults.getDisAllowedPackageList();
			for(int i = 0; i < disAllowedList.size(); i ++){
				SplitTunnelPackage app = disAllowedList.get(i);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					try {
						b.addDisallowedApplication(app.getPackageTitle());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}*/
		}catch (Exception e){
			e.printStackTrace();
		}
		return b;
	}

	// From: http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
	public static String humanReadableByteCount(VPNConnector vc, int type, boolean mbit) {
		long bytes = 0;
		if(type == 1) {
			bytes = vc.deltaStats.txBytes;
		} else if(type == 2) {
			bytes = vc.deltaStats.rxBytes;
		}
		return humanReadableByteCount(bytes, mbit);
	}
	public static String humanReadableByteCount(long bytes, boolean mbit) {
		if(mbit)
			bytes = bytes *8;
		int unit = mbit ? 1000 : 1024;
		if (bytes < unit)
			return bytes + (mbit ? " bit" : " B");

		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (mbit ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (mbit ? "" : "");
		if(mbit)
			return String.format(Locale.getDefault(),"%.1f %sbit", bytes / Math.pow(unit, exp), pre);
		else
			return String.format(Locale.getDefault(),"%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	public static String humanReadableByteCount(long bytes, boolean speed, Resources res) {
		if (speed)
			bytes = bytes * 8;
		int unit = speed ? 1000 : 1024;


		int exp = Math.max(0, Math.min((int) (Math.log(bytes) / Math.log(unit)), 3));

		float bytesUnit = (float) (bytes / Math.pow(unit, exp));

		if (speed)
			switch (exp) {
				case 0:
					return res.getString(R.string.bits_per_second, bytesUnit);
				case 1:
					return res.getString(R.string.kbits_per_second, bytesUnit);
				case 2:
					return res.getString(R.string.mbits_per_second, bytesUnit);
				default:
					return res.getString(R.string.gbits_per_second, bytesUnit);
			}
		else
			switch (exp) {
				case 0:
					return res.getString(R.string.volume_byte, bytesUnit);
				case 1:
					return res.getString(R.string.volume_kbyte, bytesUnit);
				case 2:
					return res.getString(R.string.volume_mbyte, bytesUnit);
				default:
					return res.getString(R.string.volume_gbyte, bytesUnit);

			}
	}

	public static String formatElapsedTime(long startTime) {
		StringBuilder sb = new StringBuilder();
		startTime = (new Date().getTime() - startTime) / 1000;
		if (startTime >= 60 * 60 * 24) {
			// days
			sb.append(String.format("%1$d:", startTime / (60 * 60 * 24)));
		}
		if (startTime >= 60 * 60) {
			// hours
			startTime %= 60 * 60 * 24;
			sb.append(String.format("%1$02d:", startTime / (60 * 60)));
			startTime %= 60 * 60;
		}
		// minutes:seconds
		sb.append(String.format("%1$02d:%2$02d", startTime / 60, startTime % 60));
		return sb.toString();
	}

	/* called from the activity on broadcast receipt, or startup */
	public synchronized void startActiveDialog(Context context) {
		if (mDialog != null && mDialogContext == null) {
			mDialogContext = context;
			mDialog.onStart(context);
		}
	}

	/* called when the activity shuts down (mDialog will be re-rendered when the activity starts again) */
	public synchronized void stopActiveDialog(Context context) {
		if (mDialogContext != context) {
			return;
		}
		if (mDialog != null) {
			mDialog.onStop(mDialogContext);
		}
		mDialogContext = null;
	}

	private synchronized void setDialog(Context context, UserDialog dialog) {
		mDialogContext = context;
		mDialog = dialog;
	}

	@SuppressWarnings("deprecation")
	private void updateNotification() {
		if (mDialog != null && mActivityConnections == 0 && !mNotificationActive) {
			mNotificationActive = true;

			Notification.Builder builder = new Notification.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher_background)
					.setContentTitle("User Input needed")
					.setContentText("Touch here to jump to KloudVPN")
					.setContentIntent(getMainActivityIntent());

			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(NOTIFICATION_ID, builder.getNotification());
			mNotificationActive = true;
		} else if ((mDialog == null || mActivityConnections > 0) && mNotificationActive) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(NOTIFICATION_ID);
			mNotificationActive = false;
		}
	}

	private void wakeUpActivity() {
		mHandler.post(() -> {
			Intent vpnstatus = new Intent(ACTION_VPN_STATUS);
			vpnstatus.putExtra(EXTRA_CONNECTION_STATE, mConnectionState);
			vpnstatus.putExtra(EXTRA_UUID, mUUID);
			sendBroadcast(vpnstatus, permission.ACCESS_NETWORK_STATE);


			updateNotification();

			if (mConnectionState == OpenConnectManagementThread.STATE_CONNECTED &&
					mKeepAlive == null) {
				registerKeepAlive();

			}
		});
	}

	public void updateActivityRefcount(int num) {
		mActivityConnections += num;
		updateNotification();
	}

	/* called from the VPN thread; blocks until user responds */
	public Object promptUser(UserDialog dialog) {
		Object ret;

		ret = dialog.earlyReturn();
		if (ret != null) {
			return ret;
		}

		setDialog(null, dialog);
		wakeUpActivity();
		ret = mDialog.waitForResponse();

		setDialog(null, null);
		return ret;
	}

	public synchronized void threadDone() {
		final int startId = mStartId;

		Log.i(TAG, "VPN thread has terminated");
		mVPN = null;
		mHandler.post(() -> {
			if (!stopSelfResult(startId)) {
				Log.w(TAG, "not stopping service due to startId mismatch");
			} else {
				unregisterReceivers();
			}
		});
	}

	public synchronized void setConnectionState(int state) {
		if (state == OpenConnectManagementThread.STATE_CONNECTED &&
				mConnectionState != OpenConnectManagementThread.STATE_CONNECTED) {
			startTime = new Date();

			if (t!= null){
				t.cancel();
			}
		}
		mConnectionState = state;
		wakeUpActivity();

		showNotification("Symlex layer VPN", OpenConnectService.NOTIFICATION_CHANNEL_BG_ID);
	}

	public synchronized int getConnectionState() {
		return mConnectionState;
	}

	public String getConnectionStateName() {
		return mConnectionStateNames[getConnectionState()];
	}

	public void requestStats() {
//		Log.d(TAG, "requestStats: mVPN: " + (mVPN != null));
		if (mVPN != null) {
			mVPN.requestStats();
			Log.d(TAG, "requestStats: requestStats()");
		}
	}

	public synchronized void setStats(VPNStats stats) {
		if (stats != null) {
			mStats = stats;
		}
		wakeUpActivity();
	}

	public synchronized VPNStats getStats() {
		return mStats;
	}

	public synchronized void setIPInfo(LibOpenConnect.IPInfo ipInfo, String serverName) {
		this.ipInfo = ipInfo;
		this.serverName = serverName;
	}

	public VPNLog.LogArrayAdapter getArrayAdapter(Context context) {
		return mVPNLog.getArrayAdapter(context);
	}

	public void putArrayAdapter(VPNLog.LogArrayAdapter adapter) {
		if (adapter != null) {
			mVPNLog.putArrayAdapter(adapter);
		}
	}

	public void log(final int level, final String msg) {
		Log.e(TAG, "OpenConnectManagementThread: log: level: " + level + "; msg: " + msg);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mVPNLog.add(level, msg);
			}
		});
	}

	public void clearLog() {
		mVPNLog.clear();
	}

	public String dumpLog() {
		return mVPNLog.dump();
	}

	public String getReconnectName() {
		VpnProfile p = ProfileManager.get(mUUID);
		return p == null ? null : p.getName();
	}

	public void startReconnectActivity(Context context) {
		Intent intent = new Intent(context, GrantPermissionsActivity.class);
		intent.putExtra(getPackageName() + GrantPermissionsActivity.EXTRA_UUID, mUUID);
		context.startActivity(intent);
	}

	public void stopVPN() {
		CommonUtility.isConnecting = false;
		try{
			if(disconnectHandler != null){
				disconnectHandler.removeCallbacksAndMessages(null);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		killVPNThread(true);
		ProfileManager.setConnectedVpnProfileDisconnected();
		stopWakeLock();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void jbNotificationExtras(int priority,
									  Notification.Builder nbuilder) {
		try {
			if (priority != 0) {
				Method setpriority = nbuilder.getClass().getMethod("setPriority", int.class);
				setpriority.invoke(nbuilder, priority);

				Method setUsesChronometer = nbuilder.getClass().getMethod("setUsesChronometer", boolean.class);
				setUsesChronometer.invoke(nbuilder, true);

			}

			//ignore exception
		} catch (NoSuchMethodException | IllegalArgumentException |
				 InvocationTargetException | IllegalAccessException e) {
			Log.e(TAG, "jbNotificationExtras: error: " + e.getMessage());
//			VpnStatus.logException(e);
		}

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void lpNotificationExtras(Notification.Builder nbuilder, String category) {
		nbuilder.setCategory(category);
		nbuilder.setLocalOnly(true);

	}
	private void showNotification(String tickerText, @NonNull String channel) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher_background;

		Notification.Builder nbuilder = new Notification.Builder(this);

		nbuilder.setContentTitle("VPN "+getConnectionStateName());

		nbuilder.setOnlyAlertOnce(true);
		nbuilder.setOngoing(true);

		nbuilder.setSmallIcon(icon);
		nbuilder.setContentIntent(getMainActivityIntent());

		Intent stopOpenConnectServiceIntent = new Intent("com.symlex.layer.ui.DashboardActivity");
		stopOpenConnectServiceIntent.setPackage(getPackageName());
		stopOpenConnectServiceIntent.putExtra("key_openconnect_broadcast",4);

		PendingIntent stopVPNPendingIntent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			stopVPNPendingIntent = PendingIntent.getBroadcast(getBaseContext(),1,stopOpenConnectServiceIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		} else {
			stopVPNPendingIntent = PendingIntent.getBroadcast(getBaseContext(),1,stopOpenConnectServiceIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			nbuilder.addAction(new Notification.Action.Builder(Icon.createWithResource(getBaseContext(),R.drawable.ic_cross_test),"Disconnect"/*getBaseContext().getString(R.string.disconnect)*/,stopVPNPendingIntent).build());
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
				Notification.Action action = new Notification.Action.Builder(R.drawable.ic_cross_test, "Disconnect"/*getString(R.string.disconnect)*/, stopVPNPendingIntent).build();
				nbuilder.addAction(action);
			}
		}

		// Try to set the priority available since API 16 (Jellybean)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			jbNotificationExtras(0, nbuilder);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			lpNotificationExtras(nbuilder, Notification.CATEGORY_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//noinspection NewApi
			nbuilder.setChannelId(channel);
			if (profile != null)
				//noinspection NewApi
				nbuilder.setShortcutId(profile.getUUIDString());

		}

		if (tickerText != null && !tickerText.equals(""))
			nbuilder.setTicker(tickerText);

		@SuppressWarnings("deprecation")
		Notification notification = nbuilder.getNotification();

		int notificationId = channel.hashCode();

		mNotificationManager.notify(notificationId, notification);

		startForeground(notificationId, notification);

		if (lastChannel != null && !channel.equals(lastChannel)) {
			// Cancel old notification
			mNotificationManager.cancel(lastChannel.hashCode());
		}

		if (getConnectionStateName().equals("Disconnected")){
			stopForeground(true);
		}
	}

	private void startWakeLock(){
		try{
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Symlex VPN : FreeMin");
			wl.acquire();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void stopWakeLock(){
		try{
			if (wl != null)
				wl.release();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
