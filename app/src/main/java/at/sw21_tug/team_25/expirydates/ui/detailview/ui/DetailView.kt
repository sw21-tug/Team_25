package at.sw21_tug.team_25.expirydates.ui.detailview.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
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

        private var is_editable : Boolean = false
        private var product_id : Int = 0

        fun openDetailView(activity: Activity, product: ExpItem){
            openDetailView(activity, product.id, product.name, product.date)
        }

        private fun openDetailView(activity: Activity, itemId: Int, name_string: String, date_string: String) {

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
            val closePopUpButton = popupView.findViewById<Button>(R.id.closePopUp)
            val deleteItemButton = popupView.findViewById<Button>(R.id.deleteItem)
            val dateButton = popupView.findViewById<Button>(R.id.exp_date)
            val editButton = popupView.findViewById<Button>(R.id.edit)
            val shareButton = popupView.findViewById<Button>(R.id.share)

            name.text = name_string
            nameEdit.editableText.clear()
            nameEdit.editableText.append(name_string)
            dateButton.text = date_string

            popupWindow.elevation = 10.0F
            popupWindow.isFocusable = true

            activity.findViewById<View>(android.R.id.content).post {
                val view = activity.findViewById<View>(android.R.id.content).rootView
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            }
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
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                        .deleteItemById(itemId)
                    activity.runOnUiThread {
                        popupWindow.dismiss()
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
                datePicker.show()
            }

            editButton.setOnClickListener {
                is_editable = !is_editable
                dateButton.isEnabled = is_editable
                if(is_editable) {
                    editButton.text = activity.getString(R.string.save)
                    closePopUpButton.text = activity.getString(R.string.cancel)

                    nameEdit.visibility = View.VISIBLE
                    name.visibility = View.GONE

                    nameEdit.editableText.clear()
                    nameEdit.editableText.append(name.text)
                }
                else {
                    save(nameEdit.text.toString(), dateButton.text.toString(), activity)
                    cancel(editButton, closePopUpButton, activity, nameEdit, name)

                    name.text = nameEdit.text.toString()
                }
            }


            // set on-click listener
            shareButton.setOnClickListener {
                val share = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        (activity as MainActivity).baseContext.resources.getString(R.string.shareMessage, name_string, date_string)
                    )
                    type = "text/plain"

                }, null)
                (activity as MainActivity).startActivity(share)
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


        private fun cancel(editButton: Button, closePopUpButton : Button, activity: Activity, nameEdit : EditText, name : TextView) {
            editButton.text = activity.getString(R.string.edit)
            closePopUpButton.text = activity.getString(R.string.close)
            nameEdit.visibility = View.GONE
            name.visibility = View.VISIBLE
            is_editable = false
        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        this.view.findViewById<TextView>(R.id.exp_date).text = LocalDate.of(year, month + 1, dayOfMonth).format(formatter)
    }

}