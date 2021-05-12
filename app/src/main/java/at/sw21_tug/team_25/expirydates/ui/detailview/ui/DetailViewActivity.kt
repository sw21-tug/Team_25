package at.sw21_tug.team_25.expirydates.ui.detailview.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.LinearLayout
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailViewActivity(private val activity: View) : DatePickerDialog.OnDateSetListener {
    companion object {
//        fun openDetailView(activity: Activity, product_id: Int) {
//            openDetailView(activity, "product", "date")
//        }

        private var is_editable : Boolean = false;

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

            activity.findViewById<View>(android.R.id.content).post {
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
                        activity.runOnUiThread {
                            popupWindow.dismiss()
                        }
                    }
                }

            }

            initEdit(activity, popupView)
        }


        fun initEdit(activity: Activity, popupView: View) {

            var name = popupView.findViewById<TextView>(R.id.product_name);

            var date = popupView.findViewById<Button>(R.id.exp_date);
            date.setOnClickListener{
                if(is_editable) {
                    var datePicker = DatePickerDialog(
                        activity,
                        DetailViewActivity(popupView),
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show();
                }
            }

            val edit_button = popupView.findViewById<Button>(R.id.edit);
            edit_button.setOnClickListener {
                is_editable = !is_editable;
                if(is_editable)
                    edit_button.text = "Cancel"
                else
                    edit_button.text = "Edit"
            }

        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        activity.findViewById<TextView>(R.id.exp_date).text = LocalDate.of(year, month, dayOfMonth).format(formatter)
    }

}