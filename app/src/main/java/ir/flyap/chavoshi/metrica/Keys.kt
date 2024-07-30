package ir.flyap.chavoshi.metrica

import ir.flyap.chavoshi.utill.DEBUG

object Metrica {

    val API_KEY by lazy {
        if (DEBUG) "e04f4995-3333-4146-878c-fa6649a52e64" else "e04f4995-3333-4146-878c-fa6649a57e63"
    }

}