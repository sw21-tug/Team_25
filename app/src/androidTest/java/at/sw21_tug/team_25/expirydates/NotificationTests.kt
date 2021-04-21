package at.sw21_tug.team_25.expirydates


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import at.sw21_tug.team_25.expirydates.utils.NotificationManager
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
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
        NotificationManager.displayNotification("Hello", "Hello World");
        Thread.sleep(5000)
        val notificationStackScroller = device.findObject(
                By
                .pkg("com.android.systemui")
                .res("com.android.systemui:id/notification_stack_scroller")
        )


        assertTrue(notificationStackScroller != null)
    }

    private fun clearAllNotifications() {
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        uiDevice.openNotification()
        Thread.sleep(500)
        val clearButton = uiDevice.findObject(By.desc("Clear all notifications."))

        if (clearButton != null) {
            clearButton.click()
        } else {
            uiDevice.swipe(50, uiDevice.displayHeight, 50, 2, 5);
        }
    }



}