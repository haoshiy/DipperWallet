package com.highstreet.wallet.fingerprint

import android.app.Activity
import com.highstreet.wallet.fingerprint.listener.FingerprintCallback

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
interface IFingerprint {

    fun init(context: Activity?, useFingerprint: Boolean, fingerprintCallback: FingerprintCallback?, dialogParams: DialogParams)

    fun authenticate()

    fun onStop()

    fun onDestroy()
}