package ir.flyap.music_a.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import io.appmetrica.analytics.push.AppMetricaPush
import ir.flyap.music_a.metrica.Metrica
import ir.flyap.music_a.tapsell.Tapsell
import ir.flyap.music_a.utill.debug
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

@HiltAndroidApp
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeTapsell()
        initAppMetrica()
    }

    private fun initializeTapsell() {
        TapsellPlus.initialize(this, Tapsell.ApiKey, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(adNetworks: AdNetworks) {
                debug("onInitializeSuccess")
            }

            override fun onInitializeFailed(
                adNetworks: AdNetworks,
                adNetworkError: AdNetworkError
            ) {
                debug("onInitializeFailed")
            }
        })
        TapsellPlus.setGDPRConsent(this, true)
    }

    private fun initAppMetrica() {
        val config = AppMetricaConfig.newConfigBuilder(Metrica.API_KEY).build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
        AppMetricaPush.activate(applicationContext)
    }
}