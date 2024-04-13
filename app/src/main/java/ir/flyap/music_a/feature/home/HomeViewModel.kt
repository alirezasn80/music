package ir.flyap.music_a.feature.home

import android.app.Activity
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.tapsell.Tapsell
import ir.flyap.music_a.utill.debug
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import javax.inject.Inject
import kotlin.reflect.KFunction0

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private var standardResponseId: String? = null
    private var interstitialResponseId: String? = null
    private var standardBannerContainer = mutableStateOf<ViewGroup?>(null)

    fun requestStandardAd(
        activity: Activity,
        bannerType: TapsellPlusBannerType = TapsellPlusBannerType.BANNER_320x50
    ) {
        TapsellPlus.requestStandardBannerAd(
            activity, Tapsell.StandardHome,
            bannerType,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    if (activity.isDestroyed) return
                    standardResponseId = tapsellPlusAdModel.responseId
                    showStandardAd(activity)
                }

                override fun error(message: String) {
                    if (activity.isDestroyed) return
                    debug("error in request ad : $message")
                }
            })

    }

    fun requestInterstitialAd(activity: Activity) {
        TapsellPlus.requestInterstitialAd(
            activity, Tapsell.Interstitial,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    if (activity.isDestroyed) return
                    interstitialResponseId = tapsellPlusAdModel.responseId
                }

                override fun error(message: String) {
                    if (activity.isDestroyed) return
                }
            })
    }

    fun showStandardAd(activity: Activity) {
        standardBannerContainer.value?.let { container ->
            TapsellPlus.showStandardBannerAd(activity, standardResponseId, container,
                object : AdShowListener() {
                    override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel)
                    }

                    override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel)
                        debug("error show ad : $tapsellPlusErrorModel")
                    }
                })
            // isShowButtonEnabled.value = false
        }

    }

    fun showInterstitialAd(activity: Activity, navToDetail: ()->Unit) {
        TapsellPlus.showInterstitialAd(activity, interstitialResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                    navToDetail()
                }

                override fun onRewarded(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onRewarded(tapsellPlusAdModel)
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                    navToDetail()
                }
            })
    }


    fun updateStandardBannerContainer(viewGroup: ViewGroup) {
        standardBannerContainer.value = viewGroup
    }
}