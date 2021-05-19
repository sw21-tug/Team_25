package at.sw21_tug.team_25.expirydates.ui.detailview.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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

class DetailView(private val view: View) : DatePickerDialog.OnDateSetListener {

    companion object {

        private var is_editable : Boolean = false;
        private var product_id : Int = 0;

        fun openDetailView(activity: Activity, product: ExpItem){
            openDetailView(activity, product.id, product.name, product.date)
        }

        fun openDetailView(activity: Activity, itemId: Int, name_string: String, date_string: String) {

            val inflater: LayoutInflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.fragment_detail_view, null)

            product_id = itemId

            val popupWindow = PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            val name = popupView.findViewById<TextView>(R.id.product_name)
            val nameEdit = popupView.findViewById<EditText>(R.id.product_name_edit)
            val date = popupView.findViewById<TextView>(R.id.exp_date)
            val closePopUpButton = popupView.findViewById<Button>(R.id.closePopUp)
            val deleteItemButton = popupView.findViewById<Button>(R.id.deleteItem)
            val dateButton = popupView.findViewById<Button>(R.id.exp_date);
            val editButton = popupView.findViewById<Button>(R.id.edit);

            name.text = name_string
            nameEdit.setText(name_string)
            date.text = date_string

            popupWindow.elevation = 10.0F

            activity.findViewById<View>(android.R.id.content).post {
                val view = activity.findViewById<View>(android.R.id.content).rootView
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                // set on-click listener
                closePopUpButton.setOnClickListener {
                    if(!is_editable)
                        popupWindow.dismiss()
                    else{
                        cancel(editButton, closePopUpButton, activity, nameEdit, name)
                    }
                }

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

            dateButton.setOnClickListener {
                val datePicker = DatePickerDialog(
                    activity,
                    DetailView(popupView),
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )

                datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
                datePicker.datePicker.maxDate = Long.MAX_VALUE
                datePicker.show();
            }

            editButton.setOnClickListener {
                is_editable = !is_editable;
                date.isEnabled = is_editable
                if(is_editable) {
                    editButton.text = activity.getString(R.string.save)
                    closePopUpButton.text = activity.getString(R.string.cancel)

                    nameEdit.visibility = View.VISIBLE
                    name.visibility = View.GONE
                }
                else {
                    save(nameEdit.text.toString(), date.text.toString(), activity)
                    cancel(editButton, closePopUpButton, activity, nameEdit, name)
                }
            }

        }

        private fun save(productName: String, date: String, activity: Activity) {
            val db: ExpItemDatabase = ExpItemDatabase.getDatabase(activity.applicationContext)
            val expItemDao: ExpItemDao = db.expItemDao()
            val expItem = ExpItem(productName, date)
            expItem.id = product_id
            @Suppress("DeferredResultUnused")
            GlobalScope.async {
                expItemDao.updateItem(expItem)
            }
        }


        fun cancel(editButton: Button, closePopUpButton : Button, activity: Activity, nameEdit : EditText, name : TextView) {
            editButton.text = activity.getString(R.string.edit)
            closePopUpButton.text = activity.getString(R.string.close)
            nameEdit.visibility = View.GONE
            name.visibility = View.VISIBLE
        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        this.view.findViewById<TextView>(R.id.exp_date).text = LocalDate.of(year, month + 1, dayOfMonth).format(formatter)
    }

}