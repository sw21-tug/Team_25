package at.sw21_tug.team_25.expirydates.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpItemViewModel(application: Application): AndroidViewModel(application) {
    private val readAllItems: LiveData<List<ExpItem>>
    private val repository: ExpItemRepository

    init {
        val expItemDao = ExpItemDatabase.getDatabase(application).expItemDao()
        repository = ExpItemRepository(expItemDao)
        readAllItems = repository.readAllItems
    }

    fun addItem(expItem: ExpItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItem(expItem)
        }
    }
}