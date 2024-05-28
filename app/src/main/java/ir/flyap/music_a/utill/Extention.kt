package ir.flyap.music_a.utill

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import io.appmetrica.analytics.AppMetrica
import ir.flyap.music_a.R
import ir.flyap.music_a.model.Music
import okhttp3.Interceptor
import okio.Buffer
import okio.GzipSource
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.math.floor


fun timeStampToDuration(position: Long): String {
    val totalSeconds = floor(position / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)

    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}

fun createImageBitmap(context: Context, path: String): ImageBitmap {
    return BitmapFactory.decodeStream(context.assets.open(path)).asImageBitmap()
}

fun Context.shareFile(music: Music) {
    val authority = "$packageName.fileprovider"

    try {
        val inputStream = assets.open(music.fileName)
        val tempFile = File(filesDir, "${music.displayName}.mp3")
        val outputStream = FileOutputStream(tempFile)

        // Copy the file content
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        // Close the streams
        inputStream.close()
        outputStream.close()

        // Get the URI of the temporary file using FileProvider
        val fileUri = FileProvider.getUriForFile(this@shareFile, authority, tempFile)

        // Create and start the sharing intent
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("audio/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share file using"))
        tempFile.deleteOnExit()
    } catch (e: IOException) {
        debug("error share : ${e.message}")
    }

}


fun saveInMusics(
    context: Context,
    music: Music,
    onSuccess: (Int) -> Unit,
    onFailed: () -> Unit,
) {
    try {
        val tempFile = File(context.filesDir, "${music.displayName}.mp3")
        val message = R.string.music_saved
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val file = File(folder, music.displayName)
        val fos = FileOutputStream(file)
        tempFile.inputStream().use { it.copyTo(fos) }
        fos.close()
        onSuccess(message)
    } catch (e: Exception) {
        onFailed()
        debug("Error Save file : ${e.message}")
        // AppMetrica.reportError("Error : Save File in storage", e)
        e.printStackTrace()
    }

}

// Method to copy a file from assets to the Downloads folder
fun copyFileToDownloads(context: Context, fileName: String) {
    val assetManager: AssetManager = context.assets
    var `in`: InputStream? = null
    var out: OutputStream? = null
    try {
        // Open your asset file
        `in` = assetManager.open(fileName)

        // Set up the Downloads directory path
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outFile = File(downloadsDir, fileName)

        // Ensure the Downloads directory exists
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        // Create an output stream to write to the file in the Downloads folder
        out = FileOutputStream(outFile)

        // Copy the file contents
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    } catch (e: IOException) {
        e.printStackTrace() // Handle the exception
        debug("error : ${e.message}")
    } finally {
        // Close the streams
        try {
            `in`?.close()
            out?.close()
        } catch (e: IOException) {
            e.printStackTrace() // Handle the exception
        }
    }
}


fun Context.shareText(textId: Int) {
    try {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(textId))
            type = "text/plain"
        }
        ContextCompat.startActivity(this, Intent.createChooser(sendIntent, ""), null)
    } catch (e: Exception) {
        AppMetrica.reportError("Problem to share text with intent", e)
    }

}

fun Context.openBazaarComment() {
    try {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.setData(Uri.parse("bazaar://details?id=ir.flyap.music_a"))
        intent.setPackage("com.farsitel.bazaar")
        startActivity(intent)
    } catch (e: Exception) {
        AppMetrica.reportError("Error : Open Cafe Bazaar to send comment", e)
    }

}


fun Context.openBrowser(url:String) {
    try {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)

        startActivity(intent)
    } catch (e: Exception) {
        AppMetrica.reportError("Error : Open Browser", e)
    }

}

fun logging() = Interceptor { chain ->
    val message = StringBuilder()
    val headersToRedact = emptySet<String>()
    val request = chain.request()
    val startNs = System.nanoTime()
    val response = chain.proceed(request)
    val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
    val seconds = tookMs / 1000
    val millis = tookMs % 1000
    val time = "${if (seconds.toInt() == 0) "" else "${seconds}S and "}${millis}MS"
    val url = request.url
    val code = response.code
    val responseBody = response.body!!
    val contentLength = responseBody.contentLength()


    val headers = response.headers
    for (i in 0 until headers.size)
        if (headers.name(i) !in headersToRedact) headers.value(i)


    val source = responseBody.source()
    source.request(Long.MAX_VALUE) // Buffer the entire body.
    var buffer = source.buffer


    if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
        GzipSource(buffer.clone()).use { gzippedResponseBody ->
            buffer = Buffer()
            buffer.writeAll(gzippedResponseBody)
        }
    }

    val charset: Charset = responseBody.contentType()?.charset() ?: Charsets.UTF_8

    if (contentLength != 0L) {
        val body = buffer.clone().readString(charset)

        if (url.toString().contains(".png").not()) {

            message.append("START(url:$url, code:$code) \n")
            try {

                val indentSize = 2
                var indentation = 0

                /* for (char in body) {
                     when (char) {
                         '{', '[' -> {
                             message.append(char)
                             message.append("\n")
                             indentation += indentSize
                             message.append(" ".repeat(indentation))
                         }

                         '}', ']' -> {
                             message.append("\n")
                             indentation -= indentSize
                             message.append(" ".repeat(indentation))
                             message.append(char)
                         }

                         ',' -> {
                             message.append(char)
                             message.append("\n")
                             message.append(" ".repeat(indentation))
                         }

                         else -> {
                             message.append(char)
                         }

                     }
                 }*/

                message.append(JSONObject(body).toString(4))


            } catch (e: Exception) {
                e.printStackTrace()
            }
            message.append("\n-------------------  END ($time)  -------------------")



            debug(message.toString(), "Retrofit")
        }
    }



    response
}

