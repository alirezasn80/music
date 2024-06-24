package ir.flyap.madahi_rasooli.feature.home

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.madahi_rasooli.api.service.ApiService
import ir.flyap.madahi_rasooli.cache.datastore.DataStore
import ir.flyap.madahi_rasooli.tapsell.Tapsell
import ir.flyap.madahi_rasooli.utill.BaseViewModel
import ir.flyap.madahi_rasooli.utill.Key
import ir.flyap.madahi_rasooli.utill.debug
import ir.flyap.madahi_rasooli.utill.isOnline
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val context: Application,
    private val apiService: ApiService,
) : BaseViewModel<HomeState>(HomeState()) {
    private var standardResponseId: String? = null
    private var interstitialResponseId: String? = null
    private var standardBannerContainer = mutableStateOf<ViewGroup?>(null)

    // private var connection: CheckUpdateApp? = null

    init {
        checkUpdate()
        openAppCounter()
        getCommentStatus()
        if (isOnline(context))
            getFans()
        else{
            debug("not online")
        }
    }

    private fun getFans() {
        debug("get fans")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fans = apiService.getFans()
                debug("fans : $fans")
                state.update { it.copy(fans = fans) }
            } catch (e: Exception) {
                debug("error : ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun hideNotificationAlert() = state.update { it.copy(showNotificationAlert = false) }

    private fun checkUpdate() {
        /*connection = CheckUpdateApp(object : CheckUpdateAppListener {
            override fun needUpdate(value: Boolean) {
                state.update { it.copy(needUpdate = value) }
                connection?.let {
                    application.unbindService(it);
                    connection = null
                }

            }
        })
        val i = Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND")
        i.setPackage("com.farsitel.bazaar")
        application.bindService(i, connection!!, Context.BIND_AUTO_CREATE)*/
    }

    private fun openAppCounter() {

        viewModelScope.launch {
            // Calculate number of user open application
            var count = dataStore.getOpenAppCounter(Key.COUNTER)
            count++
            dataStore.setOpenAppCounter(Key.COUNTER, count)
            state.update { it.copy(openAppCount = count) }
        }

    }

    private fun getCommentStatus() {
        viewModelScope.launch {
            val commentStatus = dataStore.getCommentStatus(Key.COMMENT)
            state.update { it.copy(showComment = commentStatus == null) }
        }
    }

    fun hideNeedUpdate() = state.update { it.copy(needUpdate = false) }

    fun hideCommentItem(status: String) {
        viewModelScope.launch {
            dataStore.setCommentStatus(Key.COMMENT, status)
            state.update { it.copy(showComment = false) }
        }
    }

    fun setDialogKey(key: HomeDialogKey) {
        state.update { it.copy(dialogKey = key) }
    }

    fun resetOpenAppCounter() {
        viewModelScope.launch {
            dataStore.setOpenAppCounter(Key.COUNTER, 0)
            state.update { it.copy(openAppCount = 0) }
        }
    }


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

    fun showInterstitialAd(activity: Activity, navToDetail: () -> Unit) {
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