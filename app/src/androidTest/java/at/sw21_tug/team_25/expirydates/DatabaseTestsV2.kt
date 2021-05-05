package at.sw21_tug.team_25.expirydates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import at.sw21_tug.team_25.expirydates.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*

import org.junit.*
import org.junit.runner.RunWith

import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException



@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@FixMethodOrder
class DatabaseTestsV2 {
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

    private fun <T>checkLiveData(data: LiveData<T>, lambda: (b: T) -> Unit) {
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

        expItemDao.insertItem(sampleItem)

        var items = expItemDao.readAllItems()
        var itemId = 9999

        checkLiveData(items) {
            Assert.assertEquals(1, it.size)
            itemId = it[0].id
        }

        Assert.assertNotEquals(9999, itemId)
        Assert.assertEquals(2, itemId) //in preceding test an item has been already created
        expItemDao.deleteItemById(itemId)

        items = expItemDao.readAllItems()

        checkLiveData(items) {
            Assert.assertEquals(0, it.size)
        }
    }
}