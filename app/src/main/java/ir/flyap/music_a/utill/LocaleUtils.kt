package ir.flyap.music_a.utill
import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleUtils {
    fun updateResources(context: Context, language: String = "fa") {
        context.resources.apply {
            val locale = Locale(language)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, displayMetrics)
        }
    }
}