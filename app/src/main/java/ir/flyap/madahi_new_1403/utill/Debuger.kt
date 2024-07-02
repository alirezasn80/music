package ir.flyap.madahi_new_1403.utill

import android.util.Log

const val DEBUG = false

fun debug(message: String?, tag: String = "AppDebug") {
    if (DEBUG)
        Log.d(tag, "********DEBUG********\n$message")
}