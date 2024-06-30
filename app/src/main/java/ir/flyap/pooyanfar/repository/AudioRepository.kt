package ir.flyap.pooyanfar.repository


import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import io.appmetrica.analytics.AppMetrica
import ir.flyap.pooyanfar.db.AppDB
import ir.flyap.pooyanfar.model.Music
import ir.flyap.pooyanfar.utill.debug
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject
constructor(
    val context: Application,
    private val db: AppDB
) {
    suspend fun getAllMusic(
        onSuccess: (List<Music>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val music = mutableListOf<Music>()
        val mmr = MediaMetadataRetriever()
        val assetManager = context.assets

        try {
            val musics = db.musicDao.getAllMusic()
            debug("db  :${musics.toString()}")

            musics.forEach { item ->

                val assetFileDescriptor = assetManager.openFd(item.musicPath)

                mmr.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)

                val uri = Uri.parse("file:///android_asset/${item.musicPath}")
                val displayName = item.title
                val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "ناشناس"
                val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""
                val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "نامشخص"
                val id = item.id.toString()
                debug("title : $title, duration  :$duration")

                music.add(
                    Music(
                        id = id,
                        uri = uri,
                        fileName = item.musicPath,
                        displayName = displayName,
                        artist = artist,
                        duration = duration.toLong(),
                        title = title,
                        imagePath = item.imagePath,
                        lyrics = item.lyrics,
                        album = item.album
                    )
                )

            }
            onSuccess(music)

        } catch (e: Exception) {
            debug("error read musics : ${e.message}")
            AppMetrica.reportError("Error in get all musics", e)
            onError(e)
        } finally {
            mmr.release()
        }

    }

    suspend fun getMusics(
        album: String,
        onSuccess: (List<Music>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val music = mutableListOf<Music>()
        val mmr = MediaMetadataRetriever()
        val assetManager = context.assets

        try {
            val musics = db.musicDao.getMusics(album)

            musics.forEach { item ->

                val assetFileDescriptor = assetManager.openFd(item.musicPath)
                mmr.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)

                val uri = Uri.parse("file:///android_asset/${item.musicPath}")
                val displayName = item.title
                val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "ناشناس"
                val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""
                val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "نامشخص"
                val id = item.id.toString()

                music.add(
                    Music(
                        id = id,
                        uri = uri,
                        fileName = item.musicPath,
                        displayName = displayName,
                        artist = artist,
                        duration = duration.toLong(),
                        title = title,
                        imagePath = item.imagePath,
                        lyrics = item.lyrics,
                        album = item.album
                    )
                )

            }
            onSuccess(music)
        } catch (e: Exception) {
            onError(e)
        } finally {
            mmr.release()
        }

    }

    suspend fun getCategories(
        onSuccess: (List<String>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val albums = mutableListOf<String>()
            albums.addAll(db.musicDao.getCategoriesByAlbum().filter { it.isNotBlank() })
            debug("album : ${albums.toString()}")
            if (albums.isNotEmpty()) {
                albums.add(0, "همه")
                onSuccess(albums)
            }
        } catch (e: Exception) {
            AppMetrica.reportError("Error in get Categories(Albums)", e)
            onError(e)
        }

    }
}


/*
*
*
*    musicFiles?.forEach { fileName ->
                val filePath = "$path/$fileName"
                val assetFileDescriptor = assetManager.openFd(filePath)
                mmr.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)

                val uri = Uri.parse("file:///android_asset/$filePath")
                val displayName = filePath.substringAfterLast('/')
                val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"
                val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "Unknown Duration"
                val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown Title"
                val id = UUID.randomUUID().toString()
                audios.add(
                    Audio(
                        id = id,
                        uri = uri,
                        displayName = displayName,
                        artist = artist,
                        duration = duration.toLong(),
                        title = title
                    )
                )

            }
*
*
* */