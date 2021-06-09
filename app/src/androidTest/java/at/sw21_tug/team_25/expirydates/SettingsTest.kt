package at.sw21_tug.team_25.expirydates

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import at.sw21_tug.team_25.expirydates.utils.GlobalSettings
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

        //Language Tests in LanguageUITest

        val textInputEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.day_selection),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.settings_popup),
                        1
                    ),
                    4
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textInputEditText.perform(ViewActions.replaceText("15"), ViewActions.closeSoftKeyboard())



        val appCompatImageButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatImageButton")),
                ViewMatchers.withContentDescription("Switch to text input mode for the time input."),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        4
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatEditText")),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.TextInputTimePickerView")),
                            1
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText.perform(ViewActions.replaceText("1"), ViewActions.closeSoftKeyboard())

        val appCompatEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatEditText")),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.TextInputTimePickerView")),
                            1
                        )
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText2.perform(ViewActions.replaceText("55"), ViewActions.closeSoftKeyboard())

        val appCompatSpinner = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatSpinner")),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.TextInputTimePickerView")),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                            3
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatSpinner.perform(click())

        val appCompatEditText3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatEditText")),
                ViewMatchers.withText("55"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.TextInputTimePickerView")),
                            1
                        )
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText3.perform(ViewActions.pressImeActionButton())

        Espresso.onView(ViewMatchers.withId(R.id.saveSettings)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        Assert.assertEquals(15, GlobalSettings.getNotificationDayOffset(mActivityTestRule.activity))
        Assert.assertEquals(1, GlobalSettings.getNotificationHour(mActivityTestRule.activity))
        Assert.assertEquals(55, GlobalSettings.getNotificationMinutes(mActivityTestRule.activity))
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


