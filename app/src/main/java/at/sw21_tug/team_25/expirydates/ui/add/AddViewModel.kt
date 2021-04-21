package at.sw21_tug.team_25.expirydates.ui.add

import androidx.lifecycle.ViewModel
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.ui.errorhandling.ErrorCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*
import java.util.Calendar.HOUR_OF_DAY

class AddViewModel : ViewModel() {
    var date : Long = 0
    var text  = ""

    fun saveValues(text: String, date: Long, dao: ExpItemDao): ErrorCode {

        if(text.isEmpty() || text.length > 255)
        {
            return ErrorCode.INPUT_ERROR
        }

        val cal = Calendar.getInstance()
        cal.set(HOUR_OF_DAY, 0)
        if(date < cal.timeInMillis)
        {
            return ErrorCode.DATE_ERROR
        }

        this.date = date
        this.text = text


        val item: ExpItem = ExpItem(text, date.toString())
        GlobalScope.async {
            dao.insertItem(item)
        }

        return ErrorCode.OK
    }


}