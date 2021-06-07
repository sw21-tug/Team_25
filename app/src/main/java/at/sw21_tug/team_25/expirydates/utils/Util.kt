package at.sw21_tug.team_25.expirydates.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun convertDateToString(long: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
            return formatter.format(long)
        }


        fun getLanguage(activity: Activity): Locale {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val languageCode = sharedPref.getString("ExpiryDate.language_preferences", "en")
            return Locale(languageCode!!)
        }

        fun setLanguage(languageCode: String, activity: Activity) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("ExpiryDate.language_preferences", languageCode)
                apply()
            }

        }

        @Suppress("DEPRECATION")
        fun setLocale(activity: Activity, languageCode: Locale) {
            Locale.setDefault(languageCode)
            val resources: Resources = activity.resources
            val config: Configuration = resources.configuration
            config.setLocale(languageCode)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        fun showToast(activity: Activity, text: String) {
            val toast = Toast.makeText(
                activity,
                text,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }
}
