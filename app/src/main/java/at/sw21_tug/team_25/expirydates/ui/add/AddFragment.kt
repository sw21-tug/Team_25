package at.sw21_tug.team_25.expirydates.ui.add

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.ui.errorhandling.ErrorCode
import at.sw21_tug.team_25.expirydates.ui.settings.SettingsView
import at.sw21_tug.team_25.expirydates.utils.ReminderScheduler
import at.sw21_tug.team_25.expirydates.utils.Util
import at.sw21_tug.team_25.expirydates.utils.Util.Companion.hideKeyboard
import at.sw21_tug.team_25.expirydates.utils.Util.Companion.showToast
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
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_menu -> {
                SettingsView.openSettingsView(this.activity as MainActivity)
            }
        }
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

            val newDate: Calendar = Calendar.getInstance()
            newDate.set(calender.year, calender.month, calender.dayOfMonth)

            when (addViewModel.saveValues(
                textView.editableText.toString(),
                newDate.timeInMillis,
                expItemDao
            )) {
                ErrorCode.INPUT_ERROR -> {
                    showToast(this.activity as MainActivity, getString(R.string.invalidInput))
                }
                ErrorCode.DATE_ERROR -> {
                    showToast(this.activity as MainActivity, getString(R.string.invalidDate))
                }
                ErrorCode.OK -> {
                    val dateString = Util.convertDateToString(newDate.timeInMillis)

                    if (Util.getLanguage(this.activity as MainActivity) == Locale("en")) {
                        showToast(
                            this.activity as MainActivity,
                            "Input: ${addViewModel.text}\nDate: $dateString"
                        )
                    } else if (Util.getLanguage(this.activity as MainActivity) == Locale("ru")) {
                        showToast(
                            this.activity as MainActivity,
                            "Вход: ${addViewModel.text}\nДата: $dateString"
                        )
                    }
                    GlobalScope.async {
                        ReminderScheduler.ensureNextReminderScheduled(this@AddFragment.requireContext())
                    }
                    textView.text = ""
                    hideKeyboard(this.activity as MainActivity, root)
                }
            }

        }
        return root
    }
}
