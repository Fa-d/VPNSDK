package org.strongswan.android

import android.content.Context
import android.content.Intent
import org.strongswan.android.data.VpnProfile
import org.strongswan.android.data.VpnProfileDataSource
import org.strongswan.android.security.LocalCertificateKeyStoreProvider
import org.strongswan.android.security.LocalCertificateStore
import java.security.Security

object Controller {

    var intent: Intent? = null
    var mProfile: VpnProfile? = null
    private var mProfileCount: Int = 0

    fun init(context: Context, intent: Intent) {
        this.intent = intent
        LocalCertificateStore.applicationContext = context
        Security.addProvider(LocalCertificateKeyStoreProvider())
        System.loadLibrary("androidbridge")
    }

    fun insertProfile(profile: VpnProfile, applicationContext: Context) {

        val mDataSource = VpnProfileDataSource(applicationContext)
        mDataSource.open()
        mProfile = mDataSource.insertProfile(profile)
        mDataSource.close()

    }

    fun startVpn(activity: Context, profile: VpnProfile) {

        mProfile = profile
        val intent = Intent(activity, VpnProfileControlActivity::class.java)
        intent.setAction(VpnProfileControlActivity.START_PROFILE)
        /*intent.putExtra(
            VpnProfileControlActivity.EXTRA_VPN_PROFILE_ID,
            mProfile.uuid.toString()
        )*/
        activity.startActivity(intent)

    }

    fun stopVpn() {

    }

}