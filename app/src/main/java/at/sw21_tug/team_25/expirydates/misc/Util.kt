package at.sw21_tug.team_25.expirydates.misc

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun convertDateToString(long: Long) : String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
            return formatter.format(long)
        }


        fun getLanguage(activity: Activity): Locale{
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val languageCode = sharedPref.getString("ExpiryDate.language_preferences", "ru")
            return Locale(languageCode)
        }

        fun setLanguage(languageCode: String, activity: Activity){
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("ExpiryDate.language_preferences", languageCode)
                apply()
            }

        }

        fun setLocale(activity: Activity, languageCode: Locale) {
            Locale.setDefault(languageCode)
            val resources: Resources = activity.resources
            val config: Configuration = resources.getConfiguration()
            config.setLocale(languageCode)
            resources.updateConfiguration(config, resources.getDisplayMetrics())
        }

    }
}