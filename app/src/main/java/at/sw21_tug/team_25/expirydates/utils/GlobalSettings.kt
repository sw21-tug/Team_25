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

    private fun getInt(activity: Activity, key: Int, defaultValue: Int): Int {
        val sharedPref = getSharedPref(activity)

        return sharedPref.getInt(
            activity.getString(key),
            defaultValue
        )
    }

    private fun setInt(activity: Activity, key: Int, value: Int) {
        val sharedPref = getSharedPref(activity)

        with(sharedPref.edit()) {
            putInt(activity.getString(key), value)
            apply()
        }
    }


    fun getNotificationDayOffset(activity: Activity, defaultValue: Int = 1): Int {
        return getInt(activity, R.string.notification_day_offset_key, defaultValue)
    }

    fun setNotificationDayOffset(activity: Activity, value: Int) {
        setInt(activity, R.string.notification_day_offset_key, value)
    }

    fun getNotificationHour(activity: Activity, defaultValue: Int = 9): Int {
        return getInt(activity, R.string.notification_hour_key, defaultValue)
    }

    fun getNotificationMinutes(activity: Activity, defaultValue: Int = 0): Int {
        return getInt(activity, R.string.notification_minutes_key, defaultValue)
    }

    fun setNotificationHour(activity: Activity, value: Int) {
        setInt(activity, R.string.notification_hour_key, value)
    }

    fun setNotificationMinutes(activity: Activity, value: Int) {
        setInt(activity, R.string.notification_minutes_key, value)
    }
}