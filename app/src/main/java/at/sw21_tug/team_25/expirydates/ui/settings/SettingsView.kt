package at.sw21_tug.team_25.expirydates.ui.settings

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.utils.GlobalSettings
import at.sw21_tug.team_25.expirydates.utils.Util
import java.util.*

class SettingsView(private val view: View) : TimePickerDialog.OnTimeSetListener {

    companion object {

        fun openSettingsView(activity: Activity) {

            val inflater: LayoutInflater =
                    activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // settings view
            val popupView = inflater.inflate(R.layout.fragment_settings, null)

            val popupWindow = PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )

            activity.findViewById<View>(android.R.id.content).post {
                val view = activity.findViewById<View>(android.R.id.content).rootView
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            }

            popupWindow.elevation = 10.0F
            popupWindow.isFocusable = true

            val dayEdit = popupView.findViewById<EditText>(R.id.day_selection)
            val closeSettingsButton = popupView.findViewById<Button>(R.id.closeSettings)
            val saveSettingsButton = popupView.findViewById<Button>(R.id.saveSettings)
            val enLanguageButton = popupView.findViewById<Button>(R.id.bt_lang_en)
            val ruLanguageButton = popupView.findViewById<Button>(R.id.bt_lang_ru)

            dayEdit.hint = (activity as MainActivity).baseContext.resources.getString(
                R.string.enter_days,
                GlobalSettings.getNotificationDayOffset(activity).toString()
            )


            popupView.findViewById<TextView>(R.id.select_time).text = (activity as MainActivity).baseContext.resources.getString(
                R.string.select_time,
                GlobalSettings.getNotificationHour(activity).toString(),
                GlobalSettings.getNotificationMinutes(activity).toString()
            )

            val time: TimePicker = popupView.findViewById(R.id.timePicker)


            enLanguageButton.setOnClickListener {
                Util.setLanguage("en", activity)
                Util.setLocale(activity, Locale("en"))
                activity.refreshCurrentFragment()
                popupWindow.dismiss()
            }

            ruLanguageButton.setOnClickListener {
                Util.setLanguage("ru", activity)
                Util.setLocale(activity, Locale("ru"))
                activity.refreshCurrentFragment()
                popupWindow.dismiss()
            }

            saveSettingsButton.setOnClickListener {
                var days = dayEdit.text.toString()
                if (days.equals(""))
                    days = "1"
                GlobalSettings.setNotificationDayOffset(activity, days.toInt())

                val hour = time.hour.toString()
                val min = time.minute.toString()
                GlobalSettings.setNotificationHour(activity, hour.toInt())
                GlobalSettings.setNotificationMinutes(activity, min.toInt())
                popupWindow.dismiss()
            }

            closeSettingsButton.setOnClickListener {
                popupWindow.dismiss()
            }
        }

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}
