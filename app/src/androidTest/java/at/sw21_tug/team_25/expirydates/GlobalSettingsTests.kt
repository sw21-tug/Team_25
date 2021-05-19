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

}