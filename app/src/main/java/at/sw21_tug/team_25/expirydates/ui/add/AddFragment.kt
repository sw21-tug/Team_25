package at.sw21_tug.team_25.expirydates.ui.add

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.misc.Util
import at.sw21_tug.team_25.expirydates.ui.errorhandling.ErrorCode
import at.sw21_tug.team_25.expirydates.utils.ReminderScheduler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class AddFragment : Fragment() {
    private lateinit var addViewModel: AddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (this.activity as MainActivity).updateTitle()
    }

    // add other menu items in language_choice_menu / choose different menu to show here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.language_choice_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_en -> {
                Util.setLanguage("en", requireActivity())
                Util.setLocale(requireActivity(), Locale("en"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }

            R.id.language_ru -> {
                Util.setLanguage("ru", requireActivity())
                Util.setLocale(requireActivity(), Locale("ru"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }
        }

        (this.activity as MainActivity).requestUpdates(R.id.navigation_add)
        return false
    }

    override fun onResume() {
        if ((this.activity as MainActivity).updateLayoutList.contains(R.id.navigation_add)) {
            (this.activity as MainActivity).updateLayoutList.remove(R.id.navigation_add)
            (this.activity as MainActivity).refreshCurrentFragment()
        }
        super.onResume()
    }

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
        val db: ExpItemDatabase = ExpItemDatabase.getDatabase(this.requireContext())
        val expItemDao: ExpItemDao = db.expItemDao()

        button.setOnClickListener {

            val newDate : Calendar = Calendar.getInstance()
            newDate.set(calender.year, calender.month, calender.dayOfMonth)

            when (addViewModel.saveValues(
                textView.editableText.toString(),
                newDate.timeInMillis,
                expItemDao
            )) {
                ErrorCode.INPUT_ERROR -> {
                    if (Util.getLanguage(this.activity as MainActivity) == Locale("en")) {
                        val toast = Toast.makeText(
                            activity,
                            "Invalid Input (Must be between 1 and 255 characters long)",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (Util.getLanguage(this.activity as MainActivity) == Locale("ru")) {
                        val toast = Toast.makeText(
                            activity,
                            "Недействительный ввод (должен содержать от 1 до 255 символов)",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }

                }
                ErrorCode.DATE_ERROR -> {
                    if (Util.getLanguage(this.activity as MainActivity) == Locale("en")) {
                        val toast = Toast.makeText(
                            activity,
                            "Invalid Date (Must not be a past date)",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (Util.getLanguage(this.activity as MainActivity) == Locale("ru")) {
                        val toast = Toast.makeText(
                            activity,
                            "Недействительная дата (не должна быть прошедшей датой)",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
                ErrorCode.OK -> {
                    val dateString = Util.convertDateToString(newDate.timeInMillis)

                    if (Util.getLanguage(this.activity as MainActivity) == Locale("en")) {
                        val toast = Toast.makeText(
                            activity,
                            "Input: " + addViewModel.text + "\n" +
                                    "Date: " + dateString,
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (Util.getLanguage(this.activity as MainActivity) == Locale("ru")) {
                        val toast = Toast.makeText(
                            activity,
                            "Вход: " + addViewModel.text + "\n" +
                                    "Дата: " + dateString,
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    val toast = Toast.makeText(activity, "Input: " + addViewModel.text + "\n" +
                            "Date: " + dateString, Toast.LENGTH_SHORT)
                    toast.show()
                    GlobalScope.async {
                        ReminderScheduler.ensureNextReminderScheduled(this@AddFragment.requireContext())
                    }
                }
            }
            textView.text = ""
        }
        return root
    }
}