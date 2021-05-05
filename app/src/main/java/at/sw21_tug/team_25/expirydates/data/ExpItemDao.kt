package at.sw21_tug.team_25.expirydates.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: ExpItem)

    @Query("SELECT * FROM items ORDER BY id ASC")
    fun readAllItems(): LiveData<List<ExpItem>>

    @Query("DELETE FROM items")
    fun deleteAllItems()

    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)
}