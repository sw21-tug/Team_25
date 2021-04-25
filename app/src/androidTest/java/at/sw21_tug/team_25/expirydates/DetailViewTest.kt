package at.sw21_tug.team_25.expirydates


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailViewTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailViewTest4() {

        onView(withId(R.id.detail_view)).perform(click())
        onView(withId(R.id.detail_view_popup)).inRoot(RootMatchers.isPlatformPopup()).check((matches(isDisplayed())))
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.detail_view_popup)).check(doesNotExist())
        return

    }

}
