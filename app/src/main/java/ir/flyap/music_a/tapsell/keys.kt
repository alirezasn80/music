package ir.flyap.music_a.tapsell

import ir.flyap.music_a.utill.DEBUG

object Tapsell {

    val ApiKey by lazy {
        if (DEBUG)
            "alsoatsrtrotpqacegkehkaiieckldhrgsbspqtgqnbrrfccrtbdomgjtahflchkqtqosa"// Test
        else
            "ejjodmradidiidoqljoeqngesrptcrfltpjjbkqsjfldbimkbqnbfqmnfetjqtnagcdsko"// Main
    }

    val StandardHome by lazy {
        if (DEBUG)
            "5cfaaa30e8d17f0001ffb294"// Test
        else
            "6613c7071eb9b937d13debbf"// Main
    }

    val StandardDetail by lazy {
        if (DEBUG)
            "5cfaaa30e8d17f0001ffb294"// Test
        else
            "66151179e7ec656e9adb83a8"// Main
    }

    val Vast by lazy {
        if (DEBUG)
            "60e441ff537bfb4073746249"// Test
        else
            "main"// Main
    }

    val NativeVideo by lazy {
        if (DEBUG)
            "60edcf8ad3459c17f019d36b"// Test
        else
            "main"// Main
    }

    val Native by lazy {
        if (DEBUG)
            "5cfaa9deaede570001d5553a"// Test
        else
            "main"// Main
    }

    val Interstitial by lazy {
        if (DEBUG)
            "5cfaa942e8d17f0001ffb292"// Test
        else
            "main"// Main
    }

    val RewardedVideo by lazy {
        if (DEBUG)
            "5cfaa942e8d17f0001ffb292"// Test
        else
            "main"// Main
    }
}
