package at.sw21_tug.team_25.expirydates.misc

import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun convertDateToString(long: Long) : String {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
            return formatter.format(long)
        }
    }
}