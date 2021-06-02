package at.sw21_tug.team_25.expirydates

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@FixMethodOrder
class SettingsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun settingsTest() {

        val settingsButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("settings"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        settingsButton.perform(click())

        Espresso.onView(ViewMatchers.withId(R.id.settings_popup)).inRoot(RootMatchers.isPlatformPopup())
            .check((ViewAssertions.matches(ViewMatchers.isDisplayed())))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}


