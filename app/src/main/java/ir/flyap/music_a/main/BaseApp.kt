package ir.flyap.music_a.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
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

        // Initialize Tapsell SDK
        initializeTapsell()
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
}