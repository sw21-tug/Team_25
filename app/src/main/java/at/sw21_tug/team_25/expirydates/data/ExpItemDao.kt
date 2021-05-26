package at.sw21_tug.team_25.expirydates.data

import androidx.lifecycle.LiveData
import androidx.room.*

object SortBy {
    const val name: String = "name"
    const val date: String = "date"
}

object SortOrder {
    const val desc: Int = 0
    const val asc: Int = 1
}

@Dao
interface ExpItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: ExpItem): Long

    @Query("SELECT * FROM items ORDER BY id ASC")
    fun readAllItems(): LiveData<List<ExpItem>>

    /**
     * Returns all items which are the next ones to expire
     */
    @Query("SELECT * FROM items WHERE date = ( SELECT date FROM items WHERE date > date('now')  GROUP BY date ORDER BY date ASC LIMIT 1 )")
    suspend fun getNextExpiringItems(): List<ExpItem>

    @Query("SELECT * FROM items WHERE id = :itemId LIMIT 1")
    suspend fun getItemByID(itemId: Long): ExpItem?

    @Query("DELETE FROM items")
    fun deleteAllItems()

    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)

    @Query(
        "SELECT * FROM items ORDER BY " +
                "CASE WHEN :sort_by = 'name' AND :sort = 0 THEN name END DESC, " +
                "CASE WHEN :sort_by = 'name' AND :sort = 1 THEN name END ASC, " +
                "CASE WHEN :sort_by = 'date' AND :sort = 0 THEN date END DESC, " +
                "CASE WHEN :sort_by = 'date' AND :sort = 1 THEN date END ASC"
    )
    fun readAllItemsSorted(sort: Int, sort_by: String): LiveData<List<ExpItem>>

    @Update
    suspend fun updateItem(item: ExpItem)
}
