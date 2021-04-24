package at.sw21_tug.team_25.expirydates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import at.sw21_tug.team_25.expirydates.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

/**
 * Example database tests, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AsyncDatabaseTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        lateinit var expItemDao: ExpItemDao
        lateinit var db: ExpItemDatabase
        lateinit var repository: ExpItemRepository
        private val testDispatcher = TestCoroutineDispatcher()
        private val testScope = TestCoroutineScope(testDispatcher)

        @BeforeClass
        @JvmStatic
        fun setup() {
            Dispatchers.setMain(testDispatcher)
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            db = Room.inMemoryDatabaseBuilder(context, ExpItemDatabase::class.java)
                    .setTransactionExecutor(testDispatcher.asExecutor())
                    .setQueryExecutor(testDispatcher.asExecutor()).build()

            expItemDao = db.expItemDao()
            repository = ExpItemRepository(expItemDao)
        }

        @AfterClass
        @JvmStatic
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }
    }

    @Before
    fun clearDb() = testScope.runBlockingTest {
        expItemDao.deleteAllItems()
    }

    @Test
    fun retrieveNextExpiringItemsTest1(): Unit = testScope.runBlockingTest {
        val item1 = ExpItem("Item1", "2021-12-01")
        val item2 = ExpItem("Item2", "2021-12-02")
        val item3 = ExpItem("Item3", "2021-12-01")
        val item4 = ExpItem("Item4", "2021-12-02")

        expItemDao.insertItem(item1)
        expItemDao.insertItem(item2)
        expItemDao.insertItem(item3)
        expItemDao.insertItem(item4)

        val items = expItemDao.getNextExpiringItems()
        Assert.assertEquals(items.size, 2)

    }

    @Test
    fun retrieveNextExpiringItemsTest2(): Unit = testScope.runBlockingTest {

        val items = expItemDao.getNextExpiringItems()

        Assert.assertEquals(items.size, 0)
    }
}