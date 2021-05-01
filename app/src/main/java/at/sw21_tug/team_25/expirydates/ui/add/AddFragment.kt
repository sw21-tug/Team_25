package at.sw21_tug.team_25.expirydates.ui.add

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.data.ExpItemRepository
import at.sw21_tug.team_25.expirydates.misc.Util
import at.sw21_tug.team_25.expirydates.ui.errorhandling.ErrorCode
import java.util.*
import kotlin.collections.ArrayList

class AddFragment : Fragment() {

    private lateinit var textUIElements: ArrayList<View>
    private lateinit var addViewModel: AddViewModel
    private lateinit var baseLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // add other menu items in language_choice_menu / choose different menu to show here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.language_choice_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_en -> {
                val toast = Toast.makeText(activity, "English clicked", Toast.LENGTH_SHORT)
                toast.show()
                Util.setLanguage("en", requireActivity())
                Util.setLocale(requireActivity(), Locale("en"))
                baseLayout.invalidate()
            }

            R.id.language_ru -> {
                val toast = Toast.makeText(activity, "Russian clicked", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        return false
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addViewModel =
                ViewModelProvider(this).get(AddViewModel::class.java)
        textUIElements = ArrayList()
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val viewLinearLayout: LinearLayout = root.findViewById(R.id.linearLayout)

        val textView: TextView = root.findViewById(R.id.text_add)
        textUIElements.add(textView)
        val button: Button = root.findViewById(R.id.button)
        textUIElements.add(button)
        val calender: DatePicker = root.findViewById(R.id.datePicker)
        textUIElements.add(calender)

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
                    val dateString = Util.convertDateToString(newDate.timeInMillis)

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