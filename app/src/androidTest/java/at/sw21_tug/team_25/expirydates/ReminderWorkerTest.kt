package at.sw21_tug.team_25.expirydates

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.work.Data
import androidx.work.testing.TestListenableWorkerBuilder
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.utils.Reminder
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReminderWorkerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun canDisplayNotificationFromWorker() {

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        Thread.sleep(500)
        NotificationTests.clearAllNotifications()
        Thread.sleep(500)

        val item1 = ExpItem("Item1", "2021-04-30")
        val item2 = ExpItem("Item2", "2021-04-30")

        val expiredItems = listOf(item1, item2)

        val ids = expiredItems.map { it.id }
        val names = expiredItems.map { it.name }


        val data = Data.Builder()
            .putIntArray("item_ids", ids.toIntArray())
            .putStringArray("item_names", names.toTypedArray())
            .build()

        val worker = TestListenableWorkerBuilder<Reminder>(
            context = context
        ).setInputData(data).build()

        runBlocking {
            val result = worker.doWork()
            Thread.sleep(500)
            device.openNotification()
            Thread.sleep(500)
            val clearButton = NotificationTests.getClearButton()

            Assert.assertTrue(clearButton != null)
            clearButton?.click()
        }
    }
}