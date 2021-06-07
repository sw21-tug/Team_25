package at.sw21_tug.team_25.expirydates


import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.utils.NotificationManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class NotificationTests {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun canDisplayNotification() {
        val device = UiDevice.getInstance(getInstrumentation())
        Thread.sleep(500)
        clearAllNotifications()
        Thread.sleep(500)
        NotificationManager.displayNotification(
            "Hello",
            "Hello World",
            ApplicationProvider.getApplicationContext()
        )
        Thread.sleep(2000)
        device.openNotification()
        Thread.sleep(500)
        val clearButton = getClearButton()

        assertTrue(clearButton != null)
        clearButton?.click()
        Thread.sleep(500)
    }

    @Test
    fun canOpenDetailViewFromNotification() {

        val appContext = ApplicationProvider.getApplicationContext<Application>()
        val expItemDao = ExpItemDatabase.getDatabase(
            appContext
        ).expItemDao()

        expItemDao.deleteAllItems()

        val name = "Salami"
        val date = "2021-04-22"

        // Create Item Object
        val item = ExpItem(name, date)

        item.id = 1

        GlobalScope.async {
            expItemDao.insertItem(item)
        }

        val uiDevice = UiDevice.getInstance(getInstrumentation())
        Thread.sleep(500)
        clearAllNotifications()
        Thread.sleep(500)
        NotificationManager.displayExpiryNotification(1, item.name, appContext)
        Thread.sleep(2000)
        uiDevice.openNotification()
        Thread.sleep(500)

        val notification = uiDevice.findObject(
            By.text(
                appContext.resources.getString(
                    R.string.expiry_notification_title,
                    name
                )
            )
        )

        notification.click()

        Espresso.onView(ViewMatchers.withId(R.id.detail_view_popup))
            .inRoot(RootMatchers.isPlatformPopup()).check(
                (ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                ))
            )
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.product_name))
            .inRoot(RootMatchers.isPlatformPopup()).check(
                ViewAssertions.matches(ViewMatchers.withText(name))
            )
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.exp_date)).inRoot(RootMatchers.isPlatformPopup())
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(date)
                )
            )
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup())
            .perform(
                ViewActions.click()
            )
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.detail_view_popup))
            .check(ViewAssertions.doesNotExist())
    }

    companion object {
        @VisibleForTesting
        fun clearAllNotifications() {
            val uiDevice = UiDevice.getInstance(getInstrumentation())
            uiDevice.openNotification()
            Thread.sleep(500)
            val clearButton = getClearButton()

            if (clearButton != null) {
                clearButton.click()
            } else {
                uiDevice.swipe(50, uiDevice.displayHeight, 50, 2, 5)
            }
        }

        @VisibleForTesting
        fun getClearButton(): UiObject2? {
            Thread.sleep(500)
            val uiDevice = UiDevice.getInstance(getInstrumentation())
            val btn = uiDevice.findObject(By.textContains("Clear"))
            if (btn != null) {
                return btn
            }
            val btn2 = uiDevice.findObject(By.textContains("CLEAR"))
            if (btn2 != null) {
                return btn2
            }
            return uiDevice.findObject(By.desc("Clear all notifications."))
        }
    }
}
