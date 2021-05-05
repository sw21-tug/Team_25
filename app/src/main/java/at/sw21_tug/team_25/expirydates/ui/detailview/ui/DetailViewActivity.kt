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
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DetailViewActivity : AppCompatActivity() {
    companion object {
//        fun openDetailView(activity: Activity, product_id: Int) {
//            openDetailView(activity, "product", "date")
//        }

        fun openDetailView(activity: Activity, product: ExpItem){
            openDetailView(activity, product.id, product.name, product.date)
        }

        fun openDetailView(activity: Activity, itemId: Int, name: String, date: String) {

            val inflater: LayoutInflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.fragment_detail_view, null)

            val popupWindow = PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            popupView.findViewById<TextView>(R.id.product_name).text = name
            popupView.findViewById<TextView>(R.id.exp_date).text = date

            popupWindow.elevation = 10.0F

            val view = activity.findViewById<View>(android.R.id.content).rootView

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            val closePopUpButton = popupView.findViewById<Button>(R.id.closePopUp)
            // set on-click listener
            closePopUpButton.setOnClickListener {
                popupWindow.dismiss()
            }

            val deleteItemButton = popupView.findViewById<Button>(R.id.deleteItem)
            // set on-click listener
            deleteItemButton.setOnClickListener {
                GlobalScope.async {
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao().deleteItemById(itemId)
                    popupWindow.dismiss()
                }
            }
        }
    }
}