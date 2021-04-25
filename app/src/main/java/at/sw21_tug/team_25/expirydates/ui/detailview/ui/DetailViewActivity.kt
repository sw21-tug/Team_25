package at.sw21_tug.team_25.expirydates.ui.detailview.ui

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItem

class DetailViewActivity : AppCompatActivity() {
    companion object {
        fun openDetailView(activity: Activity, product_id: Int) {
            //todo get product
            openDetailView(activity, "product", "date")
        }

        fun openDetailView(activity: Activity, product: ExpItem){
            openDetailView(activity, product.name, product.date)
        }

        fun openDetailView(activity: Activity, name: String, date: String) {

            val inflater: LayoutInflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater;
            var popup_view = inflater.inflate(R.layout.fragment_detail_view, null);

            var popup_window = PopupWindow(
                popup_view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            popup_view.findViewById<TextView>(R.id.product_name).text = name;
            popup_view.findViewById<TextView>(R.id.exp_date).text = date;

            popup_window.elevation = 10.0F

            val view = activity.findViewById<View>(android.R.id.content).rootView

            popup_window.showAtLocation(view, Gravity.CENTER, 0, 0)

            val closePopUpButton = popup_view.findViewById<Button>(R.id.closePopUp)
            // set on-click listener
            closePopUpButton.setOnClickListener {
                popup_window.dismiss()
            }
        }
    }
}