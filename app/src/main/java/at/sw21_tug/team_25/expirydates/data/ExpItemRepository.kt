package at.sw21_tug.team_25.expirydates.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ExpItemRepository(private val expItemDao: ExpItemDao) {

    val readAllItems: LiveData<List<ExpItem>> = expItemDao.readAllItems()

    @WorkerThread
    suspend fun insertItem(expItem: ExpItem) {
        expItemDao.insertItem(expItem)
    }
}