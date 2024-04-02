package ir.flyap.music_a.utill

import android.util.Log
import androidx.media3.ui.BuildConfig

const val DEBUG = BuildConfig.DEBUG

fun debug(message: String?, tag: String = "AppDebug") {
    if (DEBUG)
        Log.d(tag, "********DEBUG********\n$message")
}