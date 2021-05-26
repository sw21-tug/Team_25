package at.sw21_tug.team_25.expirydates

import androidx.test.rule.ActivityTestRule
import at.sw21_tug.team_25.expirydates.utils.GlobalSettings
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class GlobalSettingsTests {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun canGetAndSetNotificationDayOffsetTest() {
        GlobalSettings.setNotificationDayOffset(mActivityTestRule.activity, 5)
        var offset = GlobalSettings.getNotificationDayOffset(mActivityTestRule.activity)

        Assert.assertEquals(5, offset)

        GlobalSettings.setNotificationDayOffset(mActivityTestRule.activity, 8)
        offset = GlobalSettings.getNotificationDayOffset(mActivityTestRule.activity)

        Assert.assertEquals(8, offset)

    }

    @Test
    fun canGetAndSetNotificationHourTest() {
        GlobalSettings.setNotificationHour(mActivityTestRule.activity, 10)
        var hour = GlobalSettings.getNotificationHour(mActivityTestRule.activity)

        Assert.assertEquals(10, hour)

        GlobalSettings.setNotificationHour(mActivityTestRule.activity, 4)
        hour = GlobalSettings.getNotificationHour(mActivityTestRule.activity)

        Assert.assertEquals(4, hour)

    }

    @Test
    fun canGetAndSetNotificationMinutesTest() {
        GlobalSettings.setNotificationMinutes(mActivityTestRule.activity, 35)
        var minutes = GlobalSettings.getNotificationMinutes(mActivityTestRule.activity)

        Assert.assertEquals(35, minutes)

        GlobalSettings.setNotificationMinutes(mActivityTestRule.activity, 45)
        minutes = GlobalSettings.getNotificationMinutes(mActivityTestRule.activity)

        Assert.assertEquals(45, minutes)

    }

}
