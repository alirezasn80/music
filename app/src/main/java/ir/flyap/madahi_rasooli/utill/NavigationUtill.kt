package ir.flyap.madahi_rasooli.utill

@Suppress("RegExpRedundantEscape")
fun String.arguments(): Sequence<MatchResult> {
    val argumentRegex = "\\{(.*?)\\}".toRegex()
    return argumentRegex.findAll(this)
}

inline val String.argumentCount: Int get() = arguments().count()

object Arg {
    const val Key = "Key"
    const val FAN_ID = "FAN_ID"
    const val CATEGORY_ID = "CATEGORY_ID"
    const val CONTENT_ID = "CONTENT_ID"
    const val TITLE = "TITLE"
}

sealed interface Destination {
    object Home : Destination
    object OnBoarding : Destination
}