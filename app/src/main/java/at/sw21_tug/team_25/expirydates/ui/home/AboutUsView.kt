package at.sw21_tug.team_25.expirydates.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AboutUsView(private val view: View)  {

    companion object {

        private var is_editable: Boolean = false
        private var product_id: Int = 0

        fun openDetailView(activity: Activity) {

/*            val inflater: LayoutInflater =
                activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.fragment_detail_view, null)

            val popupWindow = PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )


            val closePopUpButton = popupView.findViewById<Button>(R.id.closePopUp)


            // set on-click listener
            closePopUpButton.setOnClickListener {

                popupWindow.dismiss()
            }*/
        }
    }

}
