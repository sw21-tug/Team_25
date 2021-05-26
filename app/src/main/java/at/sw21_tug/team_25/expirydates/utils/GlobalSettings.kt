package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import android.content.SharedPreferences
import at.sw21_tug.team_25.expirydates.R

object GlobalSettings {

    private fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
    }

    private fun getInt(context: Context, key: Int, defaultValue: Int): Int {
        val sharedPref = getSharedPref(context)

        return sharedPref.getInt(
            context.getString(key),
            defaultValue
        )
    }

    private fun setInt(context: Context, key: Int, value: Int) {
        val sharedPref = getSharedPref(context)

        with(sharedPref.edit()) {
            putInt(context.getString(key), value)
            apply()
        }
    }


    fun getNotificationDayOffset(context: Context, defaultValue: Int = 1): Int {
        return getInt(context, R.string.notification_day_offset_key, defaultValue)
    }

    fun setNotificationDayOffset(context: Context, value: Int) {
        setInt(context, R.string.notification_day_offset_key, value)
    }

    fun getNotificationHour(context: Context, defaultValue: Int = 9): Int {
        return getInt(context, R.string.notification_hour_key, defaultValue)
    }

    fun getNotificationMinutes(context: Context, defaultValue: Int = 0): Int {
        return getInt(context, R.string.notification_minutes_key, defaultValue)
    }

    fun setNotificationHour(context: Context, value: Int) {
        setInt(context, R.string.notification_hour_key, value)
    }

    fun setNotificationMinutes(context: Context, value: Int) {
        setInt(context, R.string.notification_minutes_key, value)
    }
}
