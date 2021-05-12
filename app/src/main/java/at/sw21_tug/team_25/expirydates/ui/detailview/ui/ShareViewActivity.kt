package at.sw21_tug.team_25.expirydates.ui.detailview.ui


import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R

import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.misc.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class ShareViewActivity : AppCompatActivity() {
    companion object {

        fun chooseShareOptions(activity: Activity, name: String, date: String) {

            val inflater: LayoutInflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.share_view, null)

            val popupWindow = PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )

            var message = ""
            if (Util.getLanguage(activity as MainActivity) == Locale("en")) {
                message = "Hi, I have $name that I don't need anymore and expires at $date. Do you want to have it?"
            } else if (Util.getLanguage(activity as MainActivity) == Locale("ru")) {
                message = "Привет, у меня есть $name, который мне больше не нужен, срок действия которого истекает $date. Вы хотите его иметь?"
            }

            popupView.findViewById<TextView>(R.id.share_message).text = message

            popupWindow.elevation = 10.0F

            activity.findViewById<View>(android.R.id.content).post {
                val view = activity.findViewById<View>(android.R.id.content).rootView
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            }
        }
    }
}