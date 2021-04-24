package at.sw21_tug.team_25.expirydates

import android.util.Log
import androidx.annotation.MainThread
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.data.ExpItemRepository
import at.sw21_tug.team_25.expirydates.misc.Util
import at.sw21_tug.team_25.expirydates.ui.add.AddViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


/**
 * Example database tests, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class DatabaseTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var expItemDao: ExpItemDao
    private lateinit var db: ExpItemDatabase
    private lateinit var repository: ExpItemRepository

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = ExpItemDatabase.getDatabase(context)
                //Room.databaseBuilder(context, ExpItemDatabase::class.java, "expitem-database").build()
        expItemDao = db.expItemDao()
    }


    /*
    Test to create a database Item and retrieve it.
     */
    @Test @MainThread
    @Throws(Exception::class)
    fun createTest() {
        // Delete all items in the database
        try {
            expItemDao.deleteAllItems() // This crashes the program when there are no items!
        } catch (t: Throwable){}

        // Create fake data
        val name = "Salami"
        val date = "22.04.2021"

        // Create Item Object
        val item = ExpItem(name, date)

        // Add Item to database
        @Suppress("DeferredResultUnused")
        GlobalScope.async {
            expItemDao.insertItem(item)
        }

        // Get items from database
        val items = expItemDao.readAllItems()

        val lifecycleOwner: LifecycleOwner = mockLifecycleOwner()  // Mockito.mock(LifecycleOwner::class.java)
        items.observe(lifecycleOwner, Observer<List<ExpItem>> {
            if (it != null) {
                Assert.assertTrue(
                        "The amount of items in the database is incorrect: "
                                + it.size.toString(),
                        1 == it.size)
            }
        })
    }

    /*
    Test to create a database Item and retrieve it.
     */
    @Test @MainThread
    @Throws(Exception::class)
    fun createTestUI() {
        // Delete all items in the database
        try {
            expItemDao.deleteAllItems()
        } catch (t: Throwable){}

        val viewModel = AddViewModel()

        viewModel.saveValues("test", Calendar.getInstance().timeInMillis, expItemDao)

        // Get items from database
        val items = expItemDao.readAllItems()

        val lifecycleOwner: LifecycleOwner = mockLifecycleOwner()  // Mockito.mock(LifecycleOwner::class.java)
        items.observe(lifecycleOwner, Observer<List<ExpItem>> {
            if (it != null && it.size == 1) {
                Assert.assertTrue(
                    "Text saved incorrectly: "
                            + it.get(0).name,
                    viewModel.text == it.get(0).name)

                Assert.assertTrue(
                    "Date saved incorrectly: "
                            + it.get(0).date,
                    Util.convertDateToString(viewModel.date) == it.get(0).date)
            } else {
                Log.e("DBTest","Invalid number of items in db, should be 1: " + it.size)
            }
        })
    }

    private fun mockLifecycleOwner(): LifecycleOwner {
        val owner: LifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(owner.lifecycle).thenReturn(lifecycle)
        return owner
    }


}