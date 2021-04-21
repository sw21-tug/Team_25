package at.sw21_tug.team_25.expirydates.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.data.ExpItemRepository
import at.sw21_tug.team_25.expirydates.ui.errorhandling.ErrorCode
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private lateinit var addViewModel: AddViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addViewModel =
                ViewModelProvider(this).get(AddViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val textView: TextView = root.findViewById(R.id.text_add)
        val button: Button = root.findViewById(R.id.button)
        val calender: DatePicker = root.findViewById(R.id.datePicker)

        val cal: Calendar = Calendar.getInstance()
        calender.minDate = cal.timeInMillis
        var db: ExpItemDatabase = ExpItemDatabase.getDatabase(this.requireContext())
        var expItemDao: ExpItemDao = db.expItemDao()
        var repository: ExpItemRepository = ExpItemRepository(expItemDao)


        button.setOnClickListener {

            val newDate : Calendar = Calendar.getInstance()
            newDate.set(calender.year, calender.month, calender.dayOfMonth)

            when (addViewModel.saveValues(textView.editableText.toString(), newDate.timeInMillis, expItemDao)) {
                ErrorCode.INPUT_ERROR -> {
                    val toast = Toast.makeText(activity, "Invalid Input (Must be between 1 and 255 characters long)", Toast.LENGTH_SHORT)
                    toast.show()
                }
                ErrorCode.DATE_ERROR -> {
                    val toast = Toast.makeText(activity, "Invalid Date (Must not be a past date)", Toast.LENGTH_SHORT)
                    toast.show()
                }
                ErrorCode.OK -> {
                    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
                    val dateString = formatter.format(newDate.time)

                    val toast = Toast.makeText(activity, "Input: " + addViewModel.text + "\n" +
                            "Date: " + dateString, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            textView.text = ""
        }
        return root
    }
}