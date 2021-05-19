package at.sw21_tug.team_25.expirydates.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import at.sw21_tug.team_25.expirydates.R

object GlobalSettings {

    private fun getSharedPref(activity: Activity): SharedPreferences {
        return activity.getSharedPreferences(
            activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

    }

    fun getNotificationDayOffset(activity: Activity, defaultValue: Int = 1): Int {
        val sharedPref = getSharedPref(activity)

        return sharedPref.getInt(
            activity.getString(R.string.notification_day_offset_key),
            defaultValue
        )

    }

    fun setNotificationDayOffset(activity: Activity, value: Int) {
        val sharedPref = getSharedPref(activity)

        with(sharedPref.edit()) {
            putInt(activity.getString(R.string.notification_day_offset_key), value)
            apply()
        }


    }


}