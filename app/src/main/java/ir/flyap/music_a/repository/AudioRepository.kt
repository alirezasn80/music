package ir.flyap.music_a.repository


import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.viewModelScope
import ir.flyap.music_a.db.AppDB
import ir.flyap.music_a.model.Audio
import ir.flyap.music_a.utill.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject
constructor(
    val context: Application,
    private val db: AppDB,
) {
    suspend fun getAudioData(): List<Audio> = withContext(Dispatchers.IO) {
        val path = "musics"
        val audios = mutableListOf<Audio>()
        val mmr = MediaMetadataRetriever()
        val assetManager = context.assets
        val musicFiles = assetManager.list(path)
        val musics = db.musicDao.getMusics()

        try {

            musics.forEach { item ->

                val assetFileDescriptor = assetManager.openFd(item.musicPath)
                mmr.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)

                val uri = Uri.parse("file:///android_asset/${item.musicPath}")
                val displayName = item.title
                val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"
                val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "Unknown Duration"
                val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown Title"
                val id = item.id.toString()
                audios.add(
                    Audio(
                        id = id,
                        uri = uri,
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
        } catch (e: Exception) {
            debug("error : ${e.message}")
        } finally {
            mmr.release()
        }
        audios
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