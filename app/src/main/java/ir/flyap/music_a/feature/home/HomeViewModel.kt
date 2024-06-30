package ir.flyap.music_a.feature.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.api.service.ApiService
import ir.flyap.music_a.cache.datastore.DataStore
import ir.flyap.music_a.db.AppDB
import ir.flyap.music_a.db.entity.MusicEntity
import ir.flyap.music_a.utill.BaseViewModel
import ir.flyap.music_a.utill.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

private val removeWords = listOf("دانلود", "اهنگ", "آهنگ", "مداحی", "نوحه", "روضه")

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val context: Application,
    private val db: AppDB,
    private val apiService: ApiService,
) : BaseViewModel<HomeState>(HomeState()) {
    private var myMusics = mutableListOf<MyMusic>()
    private var errorCounter = 0


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

    fun mainCrawl(artist: String) {
        myMusics = mutableListOf<MyMusic>()
        var page = 0
        val urls = mutableListOf<String>()

        var log = ""


        viewModelScope.launch(Dispatchers.IO) {

            //------------------------------------------------------LOG
            log += "crawling : in process...\n"
            state.update { it.copy(crawlLog = log) }
            //------------------------------------------------------LOG


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

                    //------------------------------------------------------LOG
                    log += "crawling : find ${results.size} items in page ${page}...\n"
                    state.update { it.copy(crawlLog = log) }
                    //------------------------------------------------------LOG


                    if (results.isEmpty())
                        break
                    else
                        urls.addAll(results)
                }


                val totalSize = urls.size

                //------------------------------------------------------LOG
                log += "crawling : total found items are ${totalSize}...\n"
                state.update { it.copy(crawlLog = log) }
                //------------------------------------------------------LOG


                // cover, title,music,lyrics

                itemCrawl(urls, artist)


            } catch (e: Exception) {
                log += "crawling : Error:${e.message}\n"
                state.update { it.copy(crawlLog = log) }
                debug("Error : ${e.message}\n")
            }
        }

    }


    fun removeWords(input: String, words: List<String>): String {
        var result = input
        for (word in words) {
            result = result.replace(word, "")
        }
        return result.trim()
    }

    fun itemCrawl(items: List<String>, artist: String) {
        val urlTimeouts = mutableListOf<String>()
        errorCounter = 0
        val totalSize = items.size
        var log = ""

        //------------------------------------------------------LOG
        log += "crawling : in process...\n"
        state.update { it.copy(crawlLog = log) }
        //------------------------------------------------------LOG


        try {

            items.forEachIndexed { index, s ->

                //----------------------------------------------------------------LOG
                log += "crawling : in process(${index + 1}/$totalSize)...\n"
                state.update { it.copy(crawlLog = log) }
                //----------------------------------------------------------------LOG

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

                        title = removeWords(title, removeWords + artist)
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
                    urlTimeouts.add(s)
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
            state.update { it.copy(crawlLog = log, myMusics = myMusics, timeouts = urlTimeouts) }
        }


    }

    //val path = Environment.getExternalStorageDirectory().path + "/seyed_crawl"
    //val imagesDir = File("$path/cover")
    //val musicDir = File("$path/music")
    fun downloadFiles(context: Context, items: List<MyMusic>) {
        var log = ""
        val imagesDir = File(context.getExternalFilesDir("images")!!.absolutePath)
        val musicDir = File(context.getExternalFilesDir("musics")!!.absolutePath)

        if (!imagesDir.exists()) imagesDir.mkdirs()

        if (!musicDir.exists()) musicDir.mkdirs()



        CoroutineScope(Dispatchers.IO).launch {
            items.forEachIndexed { index, item ->
                try {
                    val fileName = (index + 1).toString() + ".jpg"
                    val file = File(imagesDir, fileName)

                    if (!file.exists()) {

                        log += "save : start download cover ${index + 1} ...\n"
                        state.update { it.copy(saveLog = log) }

                        URL(item.cover).openStream().use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }

                        }

                        log += "done cover ${index + 1}\n"
                        state.update { it.copy(saveLog = log) }
                    }
                } catch (e: IOException) {
                    log += "Error cover ${index + 1} (${e.message})\n"
                    state.update { it.copy(saveLog = log) }
                    e.printStackTrace()
                }


                try {
                    val fileName = (index + 1).toString() + ".mp3"
                    val file = File(musicDir, fileName)

                    if (!file.exists()) {
                        log += "save : start download music ${index + 1} ...\n"
                        state.update { it.copy(saveLog = log) }
                        URL(item.music).openStream().use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }
                        }
                        log += "done music ${index + 1}\n"
                        state.update { it.copy(saveLog = log) }
                    }
                } catch (e: IOException) {
                    log += "Error Music ${index + 1} (${e.message})\n"
                    state.update { it.copy(saveLog = log) }
                    e.printStackTrace()
                }

                try {
                    log += "saving in DB...\n"
                    state.update { it.copy(saveLog = log) }
                    db.musicDao.insertMusic(
                        MusicEntity(
                            id = index + 1,
                            musicPath = "musics/${index + 1}.mp3",
                            imagePath = "images/${index + 1}.jpg",
                            title = item.title ?: "",
                            lyrics = item.lyrics ?: "",
                            album = ""
                        )
                    )
                    log += "done save in DB\n"
                    state.update { it.copy(saveLog = log) }
                } catch (e: Exception) {
                    log += "Error save in DB (${e.message})\n"
                    state.update { it.copy(saveLog = log) }
                }

            }

            log += "in backup...\n"
            state.update { it.copy(saveLog = log) }
            backupDatabase(context, "music.db")


            log += "Finish Saving\n"
            state.update { it.copy(saveLog = log) }
        }
    }

    private fun backupDatabase(context: Context, databaseName: String) {
        val dbFile = context.getDatabasePath(databaseName)
        val file = File(context.getExternalFilesDir("db_backup")!!.absolutePath, databaseName)

        if (dbFile.exists()) {
            val src = FileInputStream(dbFile).channel
            val dst = FileOutputStream(file).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
        }
    }

}


/*
*
*
*
*  fun crawlTemp() {
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
*
* */