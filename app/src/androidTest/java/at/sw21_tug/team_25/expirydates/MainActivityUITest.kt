package at.sw21_tug.team_25.expirydates


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun homeAndTabLabelsTest() {
        val textView2 = onView(
            allOf(
                withId(R.id.largeLabel), withText("Home"),
                withParent(
                    allOf(
                        withId(R.id.labelGroup),
                        withParent(
                            allOf(
                                withId(R.id.navigation_home),
                                withContentDescription("Home")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Home")))

        val textView3 = onView(
            allOf(
                withId(R.id.smallLabel), withText("Add"),
                withParent(
                    allOf(
                        withId(R.id.labelGroup),
                        withParent(
                            allOf(
                                withId(R.id.navigation_add),
                                withContentDescription("Add")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Add")))

        val textView4 = onView(
            allOf(
                withId(R.id.smallLabel), withText("List"),
                withParent(
                    allOf(
                        withId(R.id.labelGroup),
                        withParent(
                            allOf(
                                withId(R.id.navigation_list),
                                withContentDescription("List")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("List")))
    }

    @Test
    fun homeNavigationTabsUITest() {
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
        bottomNavigationItemView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withText("Add"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Add")))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_list), withContentDescription("List"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(ViewActions.click())

        val textView2 = onView(
            allOf(
                withText("List"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("List")))
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
