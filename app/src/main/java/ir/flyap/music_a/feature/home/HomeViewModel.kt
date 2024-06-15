package ir.flyap.music_a.feature.home

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.api.service.ApiService
import ir.flyap.music_a.cache.datastore.DataStore
import ir.flyap.music_a.tapsell.Tapsell
import ir.flyap.music_a.utill.BaseViewModel
import ir.flyap.music_a.utill.Key
import ir.flyap.music_a.utill.debug
import ir.flyap.music_a.utill.isOnline
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
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
        else {
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

    fun crawlTemp() {
        debug("start crawl")
        viewModelScope.launch(Dispatchers.IO) {
            val url = "https://images.search.yahoo.com/search/images?p=flower"
            val document = Jsoup.connect(url).get()
            val images = document
                // "tag#id"
                .select("section#maindoc")
                .select("section#mdoc")
                .select("section#main")
                .select("div#main-algo")
                .select("div#res-cont")
                .select("section#results")
                // "tag.class"
                .select("div.sres-cntr")
                .select("ul#sres")
                .tagName("li")
                .select("a.redesign-img.round-img")
                .select("img")
                .map {
                    it.absUrl("src")
                }
                .filter { it != null && it.isNotBlank() && it.startsWith("http") }
                .take(20)

            debug("images : ${images}")

        }
    }


    /*.select("section#maindoc")//"tag#id"
             .select("section#mdoc")
             .select("section#main")
             .select("div#main-algo")
             .select("div#res-cont")
             .select("section#results")
             .select("div.sres-cntr")//"tag.class"
             .select("ul#sres")
             .tagName("li")
             .select("a.redesign-img.round-img")
             .select("img")
             .map {
                 it.absUrl("src")
             }
             .filter { it != null && it.isNotBlank() && it.startsWith("http") }
             .take(20)*/

    fun crawl() {
        val url = "https://music-fa.com/artists/حامیم/"
        debug("start crawl")


        viewModelScope.launch(Dispatchers.IO) {
            try {


                val document = Jsoup.connect(url).get()

                // Section1----------------------------------------------- url detail page

                val result = document
                    .select("div.mf_rw")
                    .select("main.mf_home.mf_fx")
                    .select("div.mf_cntrf")
                    .select("article")
                    .select("header")
                    .select("h2")
                    .select("a")
                    .map { it.absUrl("href") }
                    .get(2)
                debug("result1 : ${result}")

                // Section2----------------------------------------------------- cover, title,music,lyrics

                val document2 = Jsoup.connect(result).get()

                var result2 = document2
                    .select("div.mf_rw")//tag.class
                    .select("main.mf_home.mf_fx")
                    .select("div.mf_cntrf")
                    .select("article.mf_pst")


                var title: String? = null
                if (result2?.hasAttr("data-song") == true)
                    title = result2.attr("data-song")

                if (title.isNullOrEmpty()){
                  title=  result2.select("header").select("h1")
                        .select("a").attr("title")
                }

                debug("title : $title")




                result2 = result2.select("div.ma_sngs")


                // Img
                val imgCover = result2
                    .select("p.mf_ax")
                    .select("img")
                    .map { it.absUrl("data-src") }
                    .first()

                // Music
                val music = result2
                    .select("p.mf_dpbx")
                    .select("span")
                    .select("a.mf_mp3")
                    .map { it.absUrl("href") }
                    .last() // quality 128k ( .first() for quality 320K )


                // Lyrics
                val lyrics = result2
                    .select("p:contains(───┤ ♩♬♫♪♭ ├───)")

                val textContent = StringBuilder()

                if (lyrics.size > 1) {
                    var currentElement = lyrics[0].nextElementSibling()
                    while (currentElement != null && currentElement != lyrics[1]) {
                        textContent.append(currentElement.text()).append("\n")
                        currentElement = currentElement.nextElementSibling()
                    }
                }


                // Title

                debug("img cover :$imgCover")

                debug("music : $music")

                debug("lyrics : $textContent")


            } catch (e: Exception) {
                debug("error:${e.message}")
            }
        }

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