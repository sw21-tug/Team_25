package at.sw21_tug.team_25.expirydates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@FixMethodOrder
class DatabaseTestsV2 {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        lateinit var expItemDao: ExpItemDao
        private lateinit var db: ExpItemDatabase
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
    fun insertItemTest(): Unit = testScope.runBlockingTest {
        val name = "Salami"
        val date = "2021-04-22"

        // Create Item Object
        val item = ExpItem(name, date)

        expItemDao.insertItem(item)

        val items = expItemDao.readAllItems()

        checkLiveData(items) {
            Assert.assertEquals(1, it.size)
            Assert.assertEquals(name, it[0].name)
            Assert.assertEquals(date, it[0].date)
        }
    }

    @Test
    fun readAllItemsEmptyTest(): Unit = testScope.runBlockingTest {
        val items = expItemDao.readAllItems()
        checkLiveData(items) {
            Assert.assertEquals(0, it.size)
        }
    }

    @Test
    fun getItemByIdTest(): Unit = testScope.runBlockingTest {
        val date = "2021-04-22"

        val item1 = ExpItem("Item1", date)
        val item2 = ExpItem("Item2", date)
        val id1 = expItemDao.insertItem(item1)
        expItemDao.insertItem(item2)
        Assert.assertNotNull(expItemDao.getItemByID(id1))
        Assert.assertEquals(item1.name, expItemDao.getItemByID(id1)?.name)
        Assert.assertNull(expItemDao.getItemByID(1000))
    }


    @Test
    fun retrieveNextExpiringItemsTest1(): Unit = testScope.runBlockingTest {

        val currentDate = LocalDate.now()

        val yesterdayDate = currentDate.minusDays(1)
        val tomorrowDate = currentDate.plusDays(1)

        val item1 = ExpItem("Item1", yesterdayDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        val item2 = ExpItem("Item2", tomorrowDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        val item3 = ExpItem("Item3", currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE))

        expItemDao.insertItem(item1)
        expItemDao.insertItem(item2)
        expItemDao.insertItem(item3)

        val items = expItemDao.getNextExpiringItems()
        Assert.assertEquals(items.size, 1)
        Assert.assertEquals(items[0].name, item2.name)

    }

    @Test
    fun retrieveNextExpiringItemsTest2(): Unit = testScope.runBlockingTest {

        val items = expItemDao.getNextExpiringItems()

        Assert.assertEquals(items.size, 0)
    }

    private fun <T> checkLiveData(data: LiveData<T>, lambda: (b: T) -> Unit) {
        val lifecycleOwner: LifecycleOwner = mockLifecycleOwner()
        val observer = Observer<T>(lambda)
        data.observe(lifecycleOwner, observer)
        data.removeObserver(observer)
    }

    private fun mockLifecycleOwner(): LifecycleOwner {
        val owner: LifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        Mockito.`when`(owner.lifecycle).thenReturn(lifecycle)
        return owner
    }

    @Test
    fun deleteSingleItem(): Unit = testScope.runBlockingTest {
        val sampleItem = ExpItem("Tomato", "2021-01-01 01:01:01")
        sampleItem.id = 123
        expItemDao.insertItem(sampleItem)

        var items = expItemDao.readAllItems()
        var itemId = 9999

        checkLiveData(items) {
            Assert.assertEquals(1, it.size)
            itemId = it[0].id
        }

        Assert.assertNotEquals(9999, itemId)
        Assert.assertEquals(123, itemId)
        expItemDao.deleteItemById(itemId)

        items = expItemDao.readAllItems()

        checkLiveData(items) {
            Assert.assertEquals(0, it.size)
        }
    }
}
