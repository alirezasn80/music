package ir.flyap.music_a.feature.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.api.service.ApiService
import ir.flyap.music_a.cache.datastore.DataStore
import ir.flyap.music_a.utill.BaseViewModel
import ir.flyap.music_a.utill.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val context: Application,
    private val apiService: ApiService,
) : BaseViewModel<HomeState>(HomeState()) {

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

    private fun connectUrl(url: String): Document? {
        return try {
            Jsoup.connect(url).get()
        } catch (e: Exception) {
            null
        }
    }

    data class MyMusic(
        val title: String?,
        val cover: String?,
        val music: String?,
        val lyrics: String?
    )

    fun crawl() {
        val myMusics = mutableListOf<MyMusic>()
        var page = 0
        val urls = mutableListOf<String>()
        val artist = "حامیم"
        var log = ""
        var errorCounter = 0

        viewModelScope.launch(Dispatchers.IO) {
            log += "crawling : in process...\n"
            state.update { it.copy(crawlLog = log) }

            try {

                while (true) {
                    page += 1
                    val url = "https://music-fa.com/artists/$artist/page/$page"
                    debug(url)

                    val document = connectUrl(url) ?: break

                    // Section1----------------------------------------------- url detail page

                    val results = document
                        .select("div.mf_rw")
                        .select("main.mf_home.mf_fx")
                        .select("div.mf_cntrf")
                        .select("article")
                        .select("header")
                        .select("h2")
                        .select("a")
                        .map { it.absUrl("href") }

                    log += "crawling : find ${results.size} items in page ${page}...\n"
                    state.update { it.copy(crawlLog = log) }

                    if (results.isEmpty())
                        break
                    else
                        urls.addAll(results)
                }


                val totalSize = urls.size

                log += "crawling : total found items are ${totalSize}...\n"
                state.update { it.copy(crawlLog = log) }


                // Section2----------------------------------------------------- cover, title,music,lyrics

                urls.forEachIndexed { index, s ->

                    log += "crawling : in process(${index + 1}/$totalSize)...\n"
                    state.update { it.copy(crawlLog = log) }

                    try {
                        val document2 = Jsoup.connect(s).get()

                        var result2 = document2
                            .select("div.mf_rw")//tag.class
                            .select("main.mf_home.mf_fx")
                            .select("div.mf_cntrf")
                            .select("article.mf_pst")


                        // Title
                        var title: String? = null
                        if (result2?.hasAttr("data-song") == true)
                            title = result2.attr("data-song")

                        if (title.isNullOrEmpty()) {
                            title = result2.select("header").select("h1")
                                .select("a").attr("title")
                        }


                        // Img

                        result2 = result2.select("div.ma_sngs")

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

                        val myMusic = MyMusic(
                            title = title,
                            cover = imgCover,
                            music = music,
                            lyrics = textContent.toString()
                        )
                        myMusics.add(myMusic)
                        debug(myMusic.toString())


                    } catch (e: Exception) {
                        errorCounter++
                        log += "crawling : Error:${e.message}\n"
                        state.update { it.copy(crawlLog = log) }
                        debug("Error : ${e.message}\n")
                    }
                }


            } catch (e: Exception) {
                log += "crawling : Error:${e.message}\n"
                state.update { it.copy(crawlLog = log) }
                debug("Error : ${e.message}\n")
            } finally {
                log += "crawling : Finish(s:${myMusics.size},e:$errorCounter)\n"
                state.update { it.copy(crawlLog = log) }
            }
        }

    }

    fun downloadFiles(context: Context, items: List<MyMusic>) {
        var log = ""
        val imagesDir = File(context.getExternalFilesDir("m_img")!!.absolutePath)
        val musicDir = File(context.getExternalFilesDir("m_music")!!.absolutePath)

        if (!imagesDir.exists()) imagesDir.mkdirs()

        if (!musicDir.exists()) musicDir.mkdirs()



        CoroutineScope(Dispatchers.IO).launch {
            items.forEachIndexed { index, item ->
                try {
                    val fileName = (index + 1).toString()
                    val file = File(imagesDir, fileName)

                    if (!file.exists()) {
                        log += "save : start download cover ${index + 1}"
                        state.update { it.copy(saveLog = log) }
                        URL(item.cover).openStream().use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }
                        }
                        log += "done cover ${index + 1}"
                        debug(log)
                    }
                } catch (e: IOException) {
                    debug("error")
                    e.printStackTrace()
                }

                debug("success")

                musicUrls.forEach { musicUrl ->
                    try {
                        val fileName = musicUrl.substring(musicUrl.lastIndexOf('/') + 1)
                        val file = File(musicDir, fileName)

                        if (!file.exists()) {
                            URL(musicUrl).openStream().use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

}