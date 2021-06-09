package at.sw21_tug.team_25.expirydates


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/* IMPORTANT!!!!
If you are writing your own tests that test the language ui, make sure to set language to EN again
at the end because otherwise other tests will not find the resources they need!
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class LanguageUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun languageUITest() {
        Locale.setDefault(Locale("en)"))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_add), withContentDescription("Add"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val overflowMenuButton = onView(
            allOf(
                withContentDescription("settings"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        overflowMenuButton.perform(click())

        onView(withId(R.id.bt_lang_ru)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.settings_popup)).check(ViewAssertions.doesNotExist())
        Thread.sleep(500)

        // Should show russian
        val button = onView(withText("Сохранить Дату"))
        button.check(matches(isDisplayed()))

        val overflowMenuButton2 = onView(
            allOf(
                withContentDescription("настройки"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        overflowMenuButton2.perform(click())

        onView(withId(R.id.bt_lang_en)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
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