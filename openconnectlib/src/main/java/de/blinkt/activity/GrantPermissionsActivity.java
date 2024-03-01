/*
 * Adapted from OpenVPN for Android
 * Copyright (c) 2012-2013, Arne Schwabe
 * Copyright (c) 2013, Kevin Cernekee
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * In addition, as a special exception, the copyright holders give
 * permission to link the code of portions of this program with the
 * OpenSSL library.
 */

package de.blinkt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;

import de.blinkt.service.OpenConnectService;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class GrantPermissionsActivity extends Activity {
	private final String TAG = "GrantPermissionsAct";
	public static final String EXTRA_START_ACTIVITY = ".start_activity";
	public static final String EXTRA_UUID = ".UUID";

	private String mUUID;
	private String mStartActivity;


	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent myIntent = getIntent();
		mUUID = myIntent.getStringExtra(getPackageName() + EXTRA_UUID);
		Log.d(TAG, "onCreate: mUUID: " + mUUID);
		if (mUUID == null) {
			finish();
			return;
		}
		mStartActivity = myIntent.getStringExtra(getPackageName() + EXTRA_START_ACTIVITY);
		Log.d(TAG, "onCreate: mStartActivity: " + mStartActivity);

		Intent prepIntent;
		try {
			prepIntent = VpnService.prepare(this);
		} catch (Exception e) {
			Log.e(TAG, "onCreate: prepIntent prepare error: " + e.getMessage());
			finish();
			return;
		}

		if (prepIntent != null) {
			try {
				startActivityForResult(prepIntent, 0);
			} catch (Exception e) {
				Log.e(TAG, "onCreate: prepIntent start error: " + e.getMessage());
				finish();
				return;
			}
		} else {
			Log.d(TAG, "onCreate: starting with 0");
			onActivityResult(0, RESULT_OK, null);
		}
	}

	/* Called by Android OS after user clicks "OK" on VpnService.prepare() dialog */ 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult: requestCode: " + requestCode + "; resultCode: " + resultCode);
		setResult(resultCode);

		if (resultCode == RESULT_OK) {
	    	Intent intent = new Intent(getBaseContext(), OpenConnectService.class);
	    	intent.putExtra(OpenConnectService.EXTRA_UUID, mUUID);
	    	startService(intent);

	    	if (mStartActivity != null) {
				Log.d(TAG, "onActivityResult: mStartActivity: " + mStartActivity);
	    		intent = new Intent();
	    		intent.setClassName(this, mStartActivity);
	    		startActivity(intent);
	    	}
		}
		finish();
	}
}
