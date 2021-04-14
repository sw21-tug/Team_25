package at.sw21_tug.team_25.expirydates.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ExpiryDates"
    }
    val text: LiveData<String> = _text
}