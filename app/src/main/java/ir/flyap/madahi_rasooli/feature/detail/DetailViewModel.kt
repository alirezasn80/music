package ir.flyap.madahi_rasooli.feature.detail

import android.app.Activity
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.TextUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.madahi_rasooli.tapsell.Tapsell
import ir.flyap.madahi_rasooli.utill.BaseViewModel
import ir.flyap.madahi_rasooli.utill.debug
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
) : BaseViewModel<DetailState>(DetailState()) {
    private var responseId: String? = null
    private var standardBannerContainer = mutableStateOf<ViewGroup?>(null)

    fun setFontSizeValue(value:TextUnit){
        state.update { it.copy(fontSize = value) }
    }

    fun requestStandardAd(
        activity: Activity,
        bannerType: TapsellPlusBannerType = TapsellPlusBannerType.BANNER_320x100
    ) {
        TapsellPlus.requestStandardBannerAd(
            activity, Tapsell.StandardDetail,
            bannerType,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    if (activity.isDestroyed) return
                    responseId = tapsellPlusAdModel.responseId
                    showStandardAd(activity)
                }

                override fun error(message: String) {
                    if (activity.isDestroyed) return
                    debug("error in request ad : $message")
                }
            })

    }

    fun showStandardAd(activity: Activity) {
        standardBannerContainer.value?.let { container ->
            TapsellPlus.showStandardBannerAd(activity, responseId, container,
                object : AdShowListener() {
                    override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel)
                    }

                    override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel)
                        debug("error show ad : $tapsellPlusErrorModel")
                    }
                })
        }

    }

    fun updateContainer(viewGroup: ViewGroup) {
        standardBannerContainer.value = viewGroup
    }

}