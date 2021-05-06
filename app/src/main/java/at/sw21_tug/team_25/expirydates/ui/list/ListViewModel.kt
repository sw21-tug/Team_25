package at.sw21_tug.team_25.expirydates.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.sw21_tug.team_25.expirydates.data.ExpItem

class ListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text
    var expItems: LiveData<List<ExpItem>>?=null
}