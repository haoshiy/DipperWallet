package com.highstreet.wallet

import cat.ereza.customactivityoncrash.config.CaocConfig
import com.highstreet.lib.BaseApplication
import com.highstreet.lib.extensions.notNullSingleValue
import com.highstreet.wallet.backup.BaseData
import com.highstreet.wallet.ui.activity.CrashActivity
import com.highstreet.wallet.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (!BuildConfig.testnet) {
            CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
            CaocConfig.Builder
                .create()
                .errorActivity(CrashActivity::class.java)
                .restartActivity(WelcomeActivity::class.java)
                .apply()
        }
    }

    private var baseData: BaseData? = null

    fun getOldDB(): BaseData {
        if (baseData == null) {
            baseData = BaseData(this)
        }
        return baseData!!
    }

    companion object {
        open var instance by notNullSingleValue<App>()
    }
}